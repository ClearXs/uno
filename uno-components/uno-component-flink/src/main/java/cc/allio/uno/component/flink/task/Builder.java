package cc.allio.uno.component.flink.task;

/**
 * 任务构建器接口
 *
 * @author jiangwei
 * @date 2022/2/22 21:07
 * @since 1.0
 */
public interface Builder {

    /**
     * 对构建完成前的基础校验
     *
     * @throws IllegalArgumentException 基准校验失败抛出的异常
     */
    void benchmarkCheck();
}
