package cc.allio.uno.core.bus;

import reactor.util.concurrent.Queues;

import java.util.Queue;

/**
 * 基于事件总线的{@link TopicEvent}事件追踪器
 *
 * @author j.x
 * @since 1.1.4
 */
public class EventTracer {

    private final Queue<TopicEvent> track;

    EventTracer() {
        this.track = Queues.<TopicEvent>unbounded().get();
    }

    /**
     * queue push
     *
     * @param event event
     */
    public void push(TopicEvent event) {
        track.offer(event);
    }

    /**
     * queue pop
     *
     * @return TopicEvent
     */
    public TopicEvent pop() {
        return track.poll();
    }
}
