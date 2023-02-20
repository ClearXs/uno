package cc.allio.uno.core.task;

import java.util.Collections;
import java.util.List;

/**
 * 使用{@link Computing}批量计算任务
 *
 * @author jiangwei
 * @date 2022/2/21 13:44
 * @since 1.0
 */
public interface BatchComputingTask<T> extends Task {

    /**
     * 指定{@link Computing}集合执行这个任务
     *
     * @param computingList 待计算实例集合
     */
    void run(List<Computing<T>> computingList);

    /**
     * 指定{@link Computing}执行这个任务
     *
     * @param computing 待计算实例
     */
    default void run(Computing<T> computing) throws Exception {
        run(Collections.singletonList(computing));
    }
}
