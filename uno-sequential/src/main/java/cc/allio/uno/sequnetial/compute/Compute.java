package cc.allio.uno.sequnetial.compute;

import cc.allio.uno.sequnetial.context.SequentialContext;

/**
 * 计算属性
 *
 * @author jiangwei
 * @date 2023/5/26 16:43
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
