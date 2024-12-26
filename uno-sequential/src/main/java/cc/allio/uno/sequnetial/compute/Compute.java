package cc.allio.uno.sequnetial.compute;

import cc.allio.uno.sequnetial.context.SequentialContext;

/**
 * 计算属性
 *
 * @author j.x
 * @since 1.1.4
 */
@FunctionalInterface
public interface Compute {

    /**
     * 计算给定时序数据
     *
     * @param context 上下文
     */
    void cal(SequentialContext context);
}
