package cc.allio.uno.netty.filter;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * 一个过滤器链的加载器
 *
 * @author j.x
 * @since 1.0
 */
public class FilterChainLoader {

    private FilterChainLoader() {
    }

    /**
     * 根据{@link FilterChain}的过滤器进行权重排序
     *
     * @param chain 过滤器链{@link FilterChain}
     * @return {@link FilterChain}
     */
    public static synchronized FilterChain loadPriority(FilterChain chain) {
        if (chain == null) {
            return null;
        }
        final FilterChain headChain = chain;
        // 轮询chain中所有的filter，并加入优先级队列中，随后进行重排序
        PriorityQueue<Filter> priorityFilter = new PriorityQueue<>();
        do {
            Filter filter = chain.getFilter();
            priorityFilter.add(filter);
            chain = chain.next();
        } while (chain.next() != null);
        // 重排序chain中的filter
        chain = headChain;
        do {
            chain.setFilter(priorityFilter.poll());
            chain = chain.next();
        } while (chain.next() != null);
        return headChain;
    }

    /**
     * 根据filter的权重加载filter chain
     *
     * @param filters 过滤器{@link Filter}
     * @return {@link FilterChain}
     */
    public static synchronized FilterChain loadChain(Filter... filters) {
        if (filters.length == 0) {
            return null;
        }
        PriorityQueue<Filter> priorityFilter = new PriorityQueue<>(Arrays.asList(filters));
        Filter filter = priorityFilter.poll();
        final FilterChain headChain = new DefaultFilterChain(filter, null);
        FilterChain chain = headChain;
        while (!priorityFilter.isEmpty()) {
            DefaultFilterChain filterChain = new DefaultFilterChain(priorityFilter.poll(), null);
            chain.setNext(filterChain);
            chain = filterChain;
        }
        return headChain;
    }
}
