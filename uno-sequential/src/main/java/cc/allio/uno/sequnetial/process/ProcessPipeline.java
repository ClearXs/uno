package cc.allio.uno.sequnetial.process;

import cc.allio.uno.sequnetial.context.SequentialContext;
import cc.allio.uno.sequnetial.process.handle.ProcessHandler;

/**
 * 处理流水线，在时序数据处理中
 *
 * @author j.x
 */
public interface ProcessPipeline {

    /**
     * 向流水线中添加handler
     *
     * @param handlers handler数组
     */
    void adds(ProcessHandler... handlers);

    /**
     * 激活该流水线
     */
    void active();

    /**
     * 获取当前处理链的时序上下文
     *
     * @return 上下文实例
     */
    SequentialContext getContext();
}
