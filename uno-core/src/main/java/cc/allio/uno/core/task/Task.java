package cc.allio.uno.core.task;

/**
 * 任务
 *
 * @author j.x
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
