package cc.allio.uno.data.orm.executor.handler;

import cc.allio.uno.data.orm.executor.options.ExecutorOptions;

/**
 * 借鉴spring aware机制，实现该接口的实例可以获取到ExecutorOptions实例
 *
 * @author jiangwei
 * @date 2024/2/14 16:21
 * @since 1.1.6
 */
public interface ExecutorOptionsAware {

    /**
     * set executorOptions
     *
     * @param executorOptions executorOptions
     */
    void setExecutorOptions(ExecutorOptions executorOptions);
}
