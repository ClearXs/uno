package cc.allio.uno.sequnetial.process.handle;

import cc.allio.uno.core.util.id.IdGenerator;
import cc.allio.uno.sequnetial.context.SequentialContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * 抽象处理器
 *
 * @author jw
 * @date 2021/12/13 16:38
 */
@Slf4j
public abstract class AbstractProcessHandler implements ProcessHandler {

    /**
     * 处理器id
     */
    private final Long handleId;

    protected AbstractProcessHandler() {
        this.handleId = IdGenerator.defaultGenerator().getNextId();
    }

    @Override
    public void init() {
        log.debug("Process handler: {} execute empty init()", this.getClass().getSimpleName());
    }

    @Override
    public Predicate<SequentialContext> filter() {
        return this::doOnNext;
    }

    @Override
    public void handle(SequentialContext context, ProcessHandlerChain next) {
        doPreHandle(context);
        doHandle(context);
        doPostHandle(context);
        if (next != null) {
            next.process(context);
        }
    }

    /**
     * 是否需要继续执行当前执行链节点的，默认允许
     *
     * @param context 执行链的上下问对象
     * @return 判读是否能够向下返回的结果
     */
    protected boolean doOnNext(SequentialContext context) {
        return true;
    }

    /**
     * 当前Handler的后处理，用于处理前的准备
     *
     * @param context 处理链上下文对象
     */
    protected abstract void doPreHandle(SequentialContext context);

    /**
     * 具体业务处理
     *
     * @param context 处理链上下文对象
     */
    protected abstract void doHandle(SequentialContext context);

    /**
     * 当前Handler的后处理，用于非关键处理与处理的收尾工作
     *
     * @param context 执行链的上下文对象
     */
    protected abstract void doPostHandle(SequentialContext context);

    @Override
    public void finish() {
        log.debug("Process handler: {} execute empty finish()", this.getClass().getSimpleName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractProcessHandler that = (AbstractProcessHandler) o;
        return Objects.equals(handleId, that.handleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(handleId);
    }

    @Override
    public String toString() {
        return "Handler{" +
                "handleId=" + handleId +
                '}';
    }
}
