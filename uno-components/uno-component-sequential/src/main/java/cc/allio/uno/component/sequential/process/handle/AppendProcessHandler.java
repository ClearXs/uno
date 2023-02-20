package cc.allio.uno.component.sequential.process.handle;

import cc.allio.uno.component.sequential.context.SequentialContext;
import cc.allio.uno.component.sequential.process.ProcessPipeline;

/**
 * 追加执行处理器，依托于存在的处理器<br/>
 * 当执行完成{@link ProcessHandler#handle(SequentialContext, ProcessHandlerChain)}后执行追加的处理器。<br/>
 * 与{@link ProcessHandler}区别有两点：<br/>
 * <i>1.数据结构采用优先级队列进行存储，而{@link ProcessHandler}采用Map进行存储</i><br/>
 * <i>2.没有使用{@link ProcessHandler}的责任链模式</i><br/>
 * 在{@link ProcessPipeline#active}执行
 *
 * @author jiangwei
 * @date 2021/12/20 10:47
 * @since 1.0
 */
public interface AppendProcessHandler extends Comparable<AppendProcessHandler> {

    /**
     * 执行追加的逻辑
     *
     * @param context 执行链上下文对象
     */
    void append(SequentialContext context);

    /**
     * 实现{@link Comparable}比较方法
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     * @see Comparable#compareTo(Object)
     */
    @Override
    default int compareTo(AppendProcessHandler o) {
        return Integer.compare(order(), o.order());
    }

    /**
     * 获取当前追加处理器的权重
     *
     * @return int类型的数字
     */
    default int order() {
        return Integer.MAX_VALUE;
    }
}
