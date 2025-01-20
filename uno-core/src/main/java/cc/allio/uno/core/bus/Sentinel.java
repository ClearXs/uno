package cc.allio.uno.core.bus;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.bus.event.Context;
import cc.allio.uno.core.bus.event.Event;
import cc.allio.uno.core.bus.event.Listener;
import cc.allio.uno.core.bus.event.Node;
import cc.allio.uno.core.util.id.IdGenerator;
import cc.allio.uno.core.util.list.LockFreeArrayList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.Atomics;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

/**
 * sentinel record each every {@link Topic} of subscriber.
 * and occurrence process on <b>publish</b>, sign of the subscription is completion.
 *
 * @author j.x
 * @since 1.2.0
 */
@Slf4j
public class Sentinel<C extends EventContext> {

    private final EventBus<C> eventBus;

    // notification Topic Key
    private final TopicKey ncKey;

    final List<WorkNode<C>> nodes;

    public static final String SIGNAL_STRING = "signal";

    final AtomicBoolean shouldSignal = new AtomicBoolean(true);

    public Sentinel(String path, EventBus<C> eventBus) {
        if (path.endsWith(SIGNAL_STRING)) {
            log.warn("The topic path is signal path, sentinel will be disabled.");
            shouldSignal.compareAndSet(true, false);
        }

        this.eventBus = eventBus;
        if (shouldSignal.get()) {
            this.ncKey = TopicKey.create(path + StringPool.SLASH + SIGNAL_STRING);
            this.nodes = new LockFreeArrayList<>(1);
        } else {
            this.ncKey = null;
            this.nodes = null;
        }
    }

    /**
     * add subscriber to sentinel cache
     *
     * @param subscriber the {@link Node} subscriber instance
     */
    public boolean attach(Node<C> subscriber) {
        if (!shouldSignal.get()) {
            return false;
        }
        return nodes.add(new WorkNode<>(subscriber));
    }

    /**
     * detach subscriber in sentinel cache
     *
     * @param subscriberId the subscriber id
     */
    public boolean detach(Long subscriberId) {
        if (!shouldSignal.get()) {
            return false;
        }

        int index = -1;
        for (int i = 0; i < nodes.size(); i++) {
            Node<C> node = nodes.get(i);
            if (node.getSubscribeId().equals(subscriberId)) {
                index = i;
            }
        }

        if (index > 0) {
            return nodes.remove(index) != null;
        }
        return true;
    }

    /**
     * when event bus publish to current topic, and topic trigger sentinel {@link #trigger(EventContext)} method.
     * the means sentinel task ({@link SignalWorker}) will be beginning works.
     *
     * @param context
     */
    public void trigger(C context) {
        if (!shouldSignal.get()) {
            return;
        }
        Worker.doSignal(List.copyOf(nodes), eventBus, context, ncKey);
    }

    /**
     * specify concrete node completion current subscription
     *
     * @param subscribeId the node subscribeId
     */
    public boolean complete(Long subscribeId) {
        if (!shouldSignal.get()) {
            return false;
        }
        return findNode(subscribeId)
                .map(WorkNode::complete)
                .orElse(false);
    }

    public Optional<WorkNode<C>> findNode(Long subscribeId) {
        return nodes.stream().filter(node -> node.getSubscribeId().equals(subscribeId)).findFirst();
    }

    /**
     * the sentinel worker
     */
    interface Worker<C extends EventContext> {

        /**
         * get unique id
         */
        Long getId();

        /**
         * stating work before action
         */
        void prepare();

        /**
         * invoke work will be doing
         *
         * @param node the node instance
         * @return true if work is finish.
         */
        boolean doing(WorkNode<?> node);

        /**
         * work of signal
         */
        static <C extends EventContext> void doSignal(List<WorkNode<?>> runningNodes,
                                                      EventBus<C> eventBus,
                                                      C context,
                                                      TopicKey ncKey) {
            new SignalWorker<>(runningNodes, eventBus, context, ncKey).prepare();
        }
    }

    /**
     * Sentinel Signal Task. non-blocking
     * <p>
     * the work done following things:
     * <ul>
     *     <ol>
     *         current new barrier of running subscriber
     *     </ol>
     *     <ol>
     *         when all subscriber completion, then trigger {@link EventBus} signal completion to event bus
     *     </ol>
     * </ul>
     */
    public static class SignalWorker<C extends EventContext> implements Worker<C> {

        private final Long id;
        private final List<WorkNode<?>> runningNodes;
        private final EventBus<C> eventBus;
        private final C context;
        private final TopicKey ncKey;
        private final Map<Long, AtomicBoolean> completed;

        SignalWorker(List<WorkNode<?>> runningNodes,
                     EventBus<C> eventBus,
                     C context,
                     TopicKey ncKey) {
            this.id = IdGenerator.defaultGenerator().getNextId();
            this.runningNodes = runningNodes;
            this.eventBus = eventBus;
            this.context = context;
            this.completed = Maps.newConcurrentMap();
            this.ncKey = ncKey;
        }

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public boolean doing(WorkNode<?> node) {
            return complete(node.getSubscribeId());
        }

        @Override
        public void prepare() {
            for (WorkNode<?> node : runningNodes) {
                // set each node completed flag is false
                this.completed.put(node.getSubscribeId(), new AtomicBoolean(false));
                // observe Work Node
                node.observe(this);
            }
        }

        public boolean complete(Long subscribeId) {
            // first check exist subscriber
            if (completed.containsKey(subscribeId)) {
                // substitution completed flag and check all is completion
                boolean workingDone =
                        completed.get(subscribeId).compareAndSet(false, true)
                                && isCompletion();
                if (workingDone) {
                    notification();
                    // reduce worker in nodes
                    for (WorkNode<?> node : runningNodes) {
                        node.finish(this);
                    }
                }
                return workingDone;
            }
            return false;
        }

        /**
         * judgment all node is subscribe is complete.
         */
        boolean isCompletion() {
            return completed.values().stream().allMatch(AtomicBoolean::get);
        }

        void notification() {
            eventBus.publish(ncKey, context);
        }
    }

    public static class WorkNode<C> implements Node<C> {

        private final Node<C> actual;

        private final List<Worker<?>> workers;

        public WorkNode(Node<C> actual) {
            this.actual = actual;
            this.workers = new LockFreeArrayList<>(1);
        }

        void observe(Worker<?> worker) {
            this.workers.add(worker);
        }

        boolean complete() {
            return workers.stream()
                    .allMatch(worker -> worker.doing(this));
        }

        void finish(Worker<?> worker) {
            workers.removeIf(w -> w.getId().equals(worker.getId()));
        }

        @Override
        public Long getSubscribeId() {
            return actual.getSubscribeId();
        }

        @Override
        public Topic<?> getTopic() {
            return actual.getTopic();
        }

        @Override
        public Long doEmmit(Consumer<C> onNext) {
            return actual.doEmmit(onNext);
        }

        @Override
        public Long doLift(LongConsumer consumer) {
            return actual.doLift(consumer);
        }

        @Override
        public Long reply(Class<? extends Event> eventType, Consumer<C> consumer) {
            return actual.reply(eventType, consumer);
        }

        @Override
        public Listener<C>[] retrieve(Class<? extends Event> eventType) {
            return actual.retrieve(eventType);
        }

        @Override
        public void release(Long listenerId) {
            actual.release(listenerId);
        }

        @Override
        public Mono<C> update(Listener<C>[] listeners, Context<C> eventContext) {
            return actual.update(listeners, eventContext);
        }

        @Override
        public Mono<C> update(Context<C> eventContext) {
            return actual.update(eventContext);
        }
    }
}
