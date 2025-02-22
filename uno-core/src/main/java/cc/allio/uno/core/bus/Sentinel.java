package cc.allio.uno.core.bus;

import cc.allio.uno.core.bus.event.Context;
import cc.allio.uno.core.bus.event.Event;
import cc.allio.uno.core.bus.event.Listener;
import cc.allio.uno.core.bus.event.Node;
import cc.allio.uno.core.util.id.IdGenerator;
import cc.allio.uno.core.util.list.LockFreeArrayList;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
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
    final List<WorkerNode<C>> subscribers;
    final AtomicBoolean shouldSignal = new AtomicBoolean(true);

    // record idled workers
    private final List<Worker<C>> idleWorkers;

    public Sentinel(TopicKey topicKey, EventBus<C> eventBus) {
        if (Worker.match(topicKey)) {
            log.warn("The topic path is signal path, sentinel will be disabled.");
            shouldSignal.compareAndSet(true, false);
        }
        this.eventBus = eventBus;
        if (shouldSignal.get()) {
            this.ncKey = SignalWorker.createTopicKey(topicKey.getPath());
            this.subscribers = new LockFreeArrayList<>(1);
            this.idleWorkers = new LockFreeArrayList<>(1);
        } else {
            this.ncKey = null;
            this.subscribers = null;
            this.idleWorkers = null;
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
        Long subscribeId = subscriber.getSubscribeId();
        for (WorkerNode<C> workerNode : subscribers) {
            if (workerNode.getSubscribeId().equals(subscribeId)) {
                log.warn("subscriber[subscribe id {} - path {}] existing current sentinel ", subscribeId, subscriber.getTopic().getPath());
                return false;
            }
        }
        return subscribers.add(new WorkerNode<>(subscriber));
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
        for (int i = 0; i < subscribers.size(); i++) {
            Node<C> node = subscribers.get(i);
            if (node.getSubscribeId().equals(subscriberId)) {
                index = i;
            }
        }

        if (index >= 0) {
            WorkerNode<C> subscriber = subscribers.remove(index);
            if (subscriber != null) {
                subscriber.release();
                return true;
            }
        }
        return false;
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
        if (subscribers.isEmpty()) {
            List<Worker<C>> temporal = Worker.kindOfAll(Collections.emptyList(), eventBus, context, ncKey);
            idleWorkers.addAll(temporal);
        }
        Worker.doSignal(List.copyOf(subscribers), eventBus, context, ncKey);
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
        // if subscriber is empty idle all worker.
        if (subscribers.isEmpty()) {
            return idleWorkers.stream().allMatch(Worker::idle);
        }
        return findNode(subscribeId).map(WorkerNode::complete).orElse(false);
    }

    public Optional<WorkerNode<C>> findNode(Long subscribeId) {
        return subscribers.stream().filter(node -> node.getSubscribeId().equals(subscribeId)).findFirst();
    }

    /**
     * the sentinel worker
     */
    public interface Worker<C extends EventContext> {

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
         * @param subscriber the subscriber instance
         * @return true if work is finish.
         */
        boolean doing(WorkerNode<?> subscriber);

        /**
         * invoke work will be doing.
         *
         * @return true if work is finish.
         */
        boolean idle();

        /**
         * worker is finish work
         *
         * @return ture is finish
         */
        boolean isFinished();

        /**
         * remove the subscriber
         *
         * @param subscriber
         */
        void remove(WorkerNode<C> subscriber);

        /**
         * of all kind worker.
         */
        static <C extends EventContext> List<Worker<C>> kindOfAll(List<WorkerNode<C>> runningSubscribers,
                                                                  EventBus<C> eventBus,
                                                                  C context,
                                                                  TopicKey ncKey) {
            SignalWorker<C> signalWorker = new SignalWorker<>(runningSubscribers, eventBus, context, ncKey);
            signalWorker.prepare();
            return List.of(signalWorker);
        }

        /**
         * work of signal
         */
        static <C extends EventContext> void doSignal(List<WorkerNode<C>> runningSubscribers,
                                                      EventBus<C> eventBus,
                                                      C context,
                                                      TopicKey ncKey) {
            new SignalWorker<>(runningSubscribers, eventBus, context, ncKey).prepare();
        }

        /**
         * determine path is match
         *
         * @param topicKey the
         * @return {@code true} if
         */
        static boolean match(TopicKey topicKey) {
            return topicKey.getPath().endsWith(SignalWorker.SIGNAL_STRING);
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
        private final List<WorkerNode<C>> runningSubscribers;
        private final EventBus<C> eventBus;
        private final C context;
        private final TopicKey ncKey;
        private final Map<Long, AtomicBoolean> completed;
        public static final String SIGNAL_STRING = "@@signal";

        SignalWorker(List<WorkerNode<C>> runningSubscribers,
                     EventBus<C> eventBus,
                     C context,
                     TopicKey ncKey) {
            this.id = IdGenerator.defaultGenerator().getNextId();
            this.runningSubscribers = new LockFreeArrayList<>(runningSubscribers);
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
        public boolean doing(WorkerNode<?> node) {
            return complete(node.getSubscribeId());
        }

        @Override
        public boolean idle() {
            onFinish();
            return true;
        }

        @Override
        public boolean isFinished() {
            return completed.values().stream().allMatch(AtomicBoolean::get);
        }

        @Override
        public void remove(WorkerNode<C> subscriber) {
            runningSubscribers.remove(subscriber);
            completed.remove(subscriber.getSubscribeId());
        }

        @Override
        public void prepare() {
            for (WorkerNode<C> subscriber : runningSubscribers) {
                // set each node completed flag is false
                this.completed.put(subscriber.getSubscribeId(), new AtomicBoolean(false));
                // observe Work Node
                subscriber.observe(this);
            }
        }

        public boolean complete(Long subscribeId) {
            // check is completion
            boolean workingDone = checkCompletion(subscribeId);
            if (workingDone) {
                onFinish();
                return true;
            }
            workingDone =
                    completed.get(subscribeId).compareAndSet(false, true)
                            && checkCompletion(subscribeId);
            if (workingDone) {
                onFinish();
            }
            return workingDone;
        }

        void onFinish() {
            // reduce worker in nodes
            for (WorkerNode<C> subscribers : runningSubscribers) {
                subscribers.finish(this);
            }
            eventBus.publish(ncKey, context).subscribe();
        }

        boolean checkCompletion(Long subscribeId) {
            return completed.containsKey(subscribeId)
                    && completed.get(subscribeId).get()
                    && isFinished();
        }

        // @signal{path}
        public static TopicKey createTopicKey(String path) {
            return TopicKey.of(path).before(SIGNAL_STRING);
        }
    }

    public static class WorkerNode<C extends EventContext> implements Node<C> {

        private final Node<C> actual;
        // record unfinished worker
        private final List<Worker<C>> workers;
        private final List<Worker<C>> finishedWorkers;

        public WorkerNode(Node<C> actual) {
            this.actual = actual;
            this.workers = new LockFreeArrayList<>(1);
            this.finishedWorkers = new LockFreeArrayList<>(1);
        }

        void observe(Worker<C> worker) {
            this.workers.add(worker);
        }

        /**
         * invoke worker node complete do work.
         *
         * @return {@code true} if workers done. otherwise workers is empty return false
         */
        boolean complete() {
            // first adjust already finish worker. and remove it
            removeWorkers();
            if (workers.isEmpty()) {
                return false;
            }
            // do work and judgment current work is finish
            AtomicBoolean done = new AtomicBoolean(false);
            for (Worker<C> worker : workers) {
                done.compareAndSet(false, worker.doing(this));
            }
            // second current workers finish.
            removeWorkers();
            return done.get();
        }

        void finish(Worker<C> worker) {
            if (workers.contains(worker)) {
                finishedWorkers.add(worker);
            }
        }

        // free-lock remove worker.
        // base-on recorded finished workers list.
        void removeWorkers() {
            // ensure finish worker from workers list remove
            for (Worker<C> worker : finishedWorkers) {
                workers.remove(worker);
            }
            // clear finish work
            finishedWorkers.clear();
        }

        // maybe occurrence illusion.
        public List<Worker<C>> getUnfinishedWorkers() {
            return workers;
        }

        // when detach worker node invoke
        void release() {
            // notify all workers remove current node
            workers.forEach(worker -> worker.remove(this));
            removeWorkers();
        }

        @Override
        public Long getSubscribeId() {
            return actual.getSubscribeId();
        }

        @Override
        public Topic<C> getTopic() {
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
