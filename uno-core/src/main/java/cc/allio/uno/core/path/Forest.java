package cc.allio.uno.core.path;

import cc.allio.uno.core.StringPool;
import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 构建一颗主题订阅森林
 *
 * @author jiangwei
 * @date 2022/6/27 10:39
 * @since 1.0
 */
@EqualsAndHashCode(of = "part")
public final class Forest<T> {

    @Getter
    private final Forest<T> parent;

    @Setter(AccessLevel.PRIVATE)
    private String part;

    @Setter(AccessLevel.PRIVATE)
    private volatile String path;

    @Setter(AccessLevel.PRIVATE)
    private volatile String[] forests;

    private final int depth;

    private final ConcurrentMap<String, Forest<T>> child = Maps.newConcurrentMap();

    private final ConcurrentMap<T, AtomicInteger> subscribers = Maps.newConcurrentMap();

    private static final AntPathMatcher matcher = new AntPathMatcher() {
        @Override
        protected String[] tokenizePath(String path) {
            return ForestMatcher.split(path);
        }
    };

    static {
        matcher.setCachePatterns(true);
        matcher.setCaseSensitive(true);
    }

    public static <T> Forest<T> createRoot() {
        return new Forest<>(null, StringPool.SLASH);
    }

    public Forest<T> append(String path) {
        if (path.equals(StringPool.SLASH) || path.equals(StringPool.EMPTY)) {
            return this;
        }
        return getOrDefault(path, Forest::new);
    }

    private Forest(Forest<T> parent, String part) {

        if (StringUtils.isEmpty(part) || part.equals(StringPool.SLASH)) {
            this.part = StringPool.EMPTY;
        } else {
            if (part.contains(StringPool.SLASH)) {
                this.ofPath(part);
            } else {
                this.part = part;
            }
        }
        this.parent = parent;
        if (null != parent) {
            this.depth = parent.depth + 1;
        } else {
            this.depth = 0;
        }
    }

    public String[] getTopics() {
        if (forests != null) {
            return forests;
        }
        return forests = ForestMatcher.split(getTopic());
    }

    public String getTopic() {
        if (path == null) {
            Forest<T> parent = getParent();
            StringBuilder builder = new StringBuilder();
            if (parent != null) {
                String parentTopic = parent.getTopic();
                builder.append(parentTopic).append(parentTopic.equals(StringPool.SLASH) ? StringPool.EMPTY : StringPool.SLASH);
            } else {
                builder.append(StringPool.SLASH);
            }
            return path = builder.append(part).toString();
        }
        return path;
    }

    public T getSubscriberOrSubscribe(Supplier<T> supplier) {
        if (subscribers.size() > 0) {
            return subscribers.keySet().iterator().next();
        }
        synchronized (this) {
            if (subscribers.size() > 0) {
                return subscribers.keySet().iterator().next();
            }
            T sub = supplier.get();
            subscribe(sub);
            return sub;
        }
    }

    public Set<T> getSubscribers() {
        return subscribers.keySet();
    }

    public boolean subscribed(T subscriber) {
        return subscribers.containsKey(subscriber);
    }

    @SafeVarargs
    public final void subscribe(T... subscribers) {
        for (T subscriber : subscribers) {
            this.subscribers.computeIfAbsent(subscriber, i -> new AtomicInteger()).incrementAndGet();
        }
    }

    @SafeVarargs
    public final List<T> unsubscribe(T... subscribers) {
        List<T> unsub = new ArrayList<>();
        for (T subscriber : subscribers) {
            this.subscribers.computeIfPresent(subscriber, (k, v) -> {
                if (v.decrementAndGet() <= 0) {
                    unsub.add(k);
                    return null;
                }
                return v;
            });
        }
        return unsub;
    }

    public final void unsubscribe(Predicate<T> predicate) {
        for (Map.Entry<T, AtomicInteger> entry : this.subscribers.entrySet()) {
            if (predicate.test(entry.getKey()) && entry.getValue().decrementAndGet() <= 0) {
                this.subscribers.remove(entry.getKey());
            }
        }
    }

    public final void unsubscribeAll() {
        this.subscribers.clear();
    }

    public Collection<Forest<T>> getChildren() {
        return child.values();
    }

    private void ofPath(String path) {
        String[] parts = path.split(StringPool.SLASH, 2);
        this.part = parts[0];
        if (parts.length > 1) {
            Forest<T> part = new Forest<>(this, parts[1]);
            this.child.put(part.part, part);
        }
    }

    private Forest<T> getOrDefault(String path, BiFunction<Forest<T>, String, Forest<T>> mapping) {
        if (path.startsWith(StringPool.SLASH)) {
            path = path.substring(1);
        }
        String[] parts = path.split(StringPool.SLASH);
        Forest<T> part = child.computeIfAbsent(parts[0], _path -> mapping.apply(this, _path));
        for (int i = 1; i < parts.length && part != null; i++) {
            Forest<T> parent = part;
            part = part.child.computeIfAbsent(parts[i], _path -> mapping.apply(parent, _path));
        }
        return part;
    }

    public Optional<Forest<T>> getTopic(String path) {
        return Optional.ofNullable(getOrDefault(path, ((forestPart, s) -> null)));
    }

    public Flux<Forest<T>> findPath(String path) {
        return Flux.create(sink -> findPath(path, sink::next, sink::complete));
    }

    public void findPath(String path,
                         Consumer<Forest<T>> sink,
                         Runnable end) {
        if (!path.startsWith(StringPool.SLASH)) {
            path = StringPool.SLASH + path;
        }
        find(ForestMatcher.split(path), this, sink, end);
    }

    @Override
    public String toString() {
        return "topic: " + getTopic() + ", subscribers: " + subscribers.size() + ", children: " + child.size();
    }

    protected boolean match(String[] pars) {
        return ForestMatcher.match(getTopics(), pars)
                || ForestMatcher.match(pars, getTopics());
    }

    public static <T> void find(String[] topicParts,
                                Forest<T> forestPart,
                                Consumer<Forest<T>> sink,
                                Runnable end) {
        ArrayDeque<Forest<T>> cache = new ArrayDeque<>(128);
        cache.add(forestPart);

        String nextPart = null;
        while (!cache.isEmpty()) {
            Forest<T> part = cache.poll();
            if (part == null) {
                break;
            }
            if (part.match(topicParts)) {
                sink.accept(part);
            }

            // 订阅了如 /test/**/event/*
            if (part.part.equals("**")) {
                Forest<T> tmp = null;
                for (int i = part.depth; i < topicParts.length; i++) {
                    tmp = part.child.get(topicParts[i]);
                    if (tmp != null) {
                        cache.add(tmp);
                    }
                }
                if (null != tmp) {
                    continue;
                }
            }
            if ("**".equals(nextPart) || "*".equals(nextPart)) {
                cache.addAll(part.child.values());
                continue;
            }
            Forest<T> next = part.child.get("**");
            if (next != null) {
                cache.add(next);
            }
            next = part.child.get("*");
            if (next != null) {
                cache.add(next);
            }

            if (part.depth + 1 >= topicParts.length) {
                continue;
            }
            nextPart = topicParts[part.depth + 1];
            if (nextPart.equals("*") || nextPart.equals("**")) {
                cache.addAll(part.child.values());
                continue;
            }
            next = part.child.get(nextPart);
            if (next != null) {
                cache.add(next);
            }
        }
        end.run();
    }

    public long getTotalPath() {
        long total = child.size();
        for (Forest<T> tForest : getChildren()) {
            total += tForest.getTotalPath();
        }
        return total;
    }

    public long getTotalSubscriber() {
        long total = subscribers.size();
        for (Forest<T> tForest : getChildren()) {
            total += tForest.getTotalPath();
        }
        return total;
    }

    public Flux<Forest<T>> getAllSubscriber() {
        List<Flux<Forest<T>>> all = new ArrayList<>();

        all.add(Flux.fromIterable(this.getChildren()));

        for (Forest<T> tForest : getChildren()) {
            all.add(tForest.getAllSubscriber());
        }
        return Flux.concat(all);
    }

    public void clean() {
        unsubscribeAll();
        getChildren().forEach(Forest::clean);
        child.clear();
    }

}
