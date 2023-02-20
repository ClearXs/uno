package cc.allio.uno.core.task;

import java.util.List;

/**
 * 组合的计算任务
 *
 * @author jiangwei
 * @date 2021/12/22 15:30
 * @since 1.0
 */
public abstract class BaseComposedComputeTask<T> implements BatchComputingTask<T> {

    /**
     * 添加可以计算的任务
     *
     * @param task 计算任务实例
     * @deprecated 使用@{@link #run(Computing)}@{@link #run(List)}
     */
    @Deprecated
    protected abstract void addComputableTask(Computing<T> task);
}
