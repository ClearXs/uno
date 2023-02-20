package cc.allio.uno.core.task;

/**
 * 任务
 *
 * @author jiangwei
 * @date 2021/12/22 15:17
 * @since 1.0
 */
public interface Task {

    /**
     * 运行这个任务
     */
    void run() throws Exception;

    /**
     * 结束这个任务
     */
    void finish();
}
