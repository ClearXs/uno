package cc.allio.uno.component.sequential.process.handle;

import cc.allio.uno.component.sequential.context.SequentialContext;

/**
 * 处理链
 *
 * @author jw
 * @date 2021/12/13 15:49
 */
public interface ProcessHandlerChain {

    /**
     * 执行处理链
     *
     * @param context 处理上下文对象
     */
    void process(SequentialContext context);
}
