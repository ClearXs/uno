package cc.allio.uno.component.sequential.process;


import cc.allio.uno.component.sequential.context.SequentialContext;
import cc.allio.uno.component.sequential.process.handle.ProcessHandler;

/**
 * 处理流水线，在时序数据处理中
 *
 * @author jw
 * @date 2021/12/13 14:13
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
