package cc.allio.uno.sequnetial.process.handle;

import cc.allio.uno.core.type.Type;
import cc.allio.uno.sequnetial.Sequential;
import cc.allio.uno.sequnetial.context.SequentialContext;

import java.util.function.Predicate;

/**
 * 执行链的处理器<br/>
 * 在具体实现类中加上{@link com.google.auto.service.AutoService}注解
 *
 * @author jw
 * @date 2021/12/13 14:28
 * @see AbstractProcessHandler
 */
public interface ProcessHandler {

    /**
     * 当处理器实例化时调用
     */
    void init();

    /**
     * 判断当前Handler是否可以继续执行后面的处理过程。只影响当前处理链节点。<br/>
     * <i>如果判断为true，则执行当前处理链，否则，跳过。</i>
     *
     * @return 判断的Predicate对象
     */
    Predicate<SequentialContext> filter();

    /**
     * 具体处理链中工作的方法
     *
     * @param context 执行链上下文对象
     * @param next    下一个handler的链
     */
    void handle(SequentialContext context, ProcessHandlerChain next);

    /**
     * 处理器结束时触发
     */
    void finish();

    /**
     * 获取当前处理器的序号
     *
     * @return int类型的数字
     */
    int order();

    /**
     * 获取处理时序数据类型
     *
     * @return 处理类型
     * @throws NullPointerException 当类型为空在执行过程中将会抛出该异常
     */
    Type getType();

    /**
     * 获取当前处理器需要处理Sequential类型Class对象
     *
     * @return {@link Sequential}的子类
     * @deprecated 1.1.4删除
     */
    default Class<? extends Sequential> getTypeClass() {
        return null;
    }

}
