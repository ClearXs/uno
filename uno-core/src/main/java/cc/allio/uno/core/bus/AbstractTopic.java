package cc.allio.uno.core.bus;

import reactor.core.publisher.FluxSink;

import java.util.Objects;

/**
 * 事件总线中某个消息主题，抽象某些公共的方法
 *
 * @author jw
 * @date 2021/12/17 22:52
 */
public abstract class AbstractTopic<C> implements Topic<C> {

    /**
     * 主题路径
     */
    private final String path;

    protected AbstractTopic(String path) {
        this.path = path;
    }

    /**
     * 数据流信号
     */
    private FluxSink<C> sink;


    @Override
    public void generate(FluxSink<C> sink) {
        this.sink = sink;
    }

    @Override
    public void emmit(C context) {
        if (sink != null) {
            sink.next(context);
        }
    }

    @Override
    public String getPath() {
        // 使当前主题能够构建成某一个具体的路径，
        // 如果是test -> /test
        // 如果是par_chi -> /par/chi
        // 如果是par-chi -> /par/chi ...
        return Topic.topicPathway(this.path);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractTopic<C> that = (AbstractTopic<C>) o;
        return Objects.equals(getPath(), that.getPath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPath());
    }
}
