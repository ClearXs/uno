package cc.allio.uno.component.sequential.process;

import cc.allio.uno.component.sequential.Sequential;
import cc.allio.uno.component.sequential.context.SequentialContext;
import cc.allio.uno.component.sequential.process.handle.ProcessHandler;

import java.util.List;

/**
 * 时序数据处理器
 *
 * @author jw
 * @date 2021/12/13 14:05
 */
public interface Processor {

    /**
     * 处理时序数据
     *
     * @param context 消息总线上下文对象
     * @throws IllegalArgumentException 当{@link SequentialContext}、{@link Sequential}以及{@link Sequential#getType()}为空时抛出
     */
    void process(SequentialContext context);

    /**
     * 依据处理类型获取执行处理器列表集合
     *
     * @param expectedType 期望的类型
     * @return 获取到的列表集合
     */
    List<ProcessHandler> getProcessHandlers(String expectedType);
}
