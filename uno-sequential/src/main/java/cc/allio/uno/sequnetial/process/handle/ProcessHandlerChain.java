package cc.allio.uno.sequnetial.process.handle;

import cc.allio.uno.sequnetial.context.SequentialContext;

/**
 * 处理链
 *
 * @author j.x
 */
public interface ProcessHandlerChain {

    /**
     * 执行处理链
     *
     * @param context 处理上下文对象
     */
    void process(SequentialContext context);
}
