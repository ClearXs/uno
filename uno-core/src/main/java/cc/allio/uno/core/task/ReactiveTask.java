package cc.allio.uno.core.task;

/**
 * reactive task
 *
 * @param <T> 类型参数
 * @author jiangwei
 * @date 2023/4/28 18:25
 * @since 1.1.4
 */
public interface ReactiveTask<T> {

    /**
     * 执行这个任务，返回多流 flux
     */
    void run();

    /**
     * 完成这个任务
     */
    void complete();
}
