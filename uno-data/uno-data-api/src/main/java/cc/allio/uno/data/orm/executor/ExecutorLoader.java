package cc.allio.uno.data.orm.executor;

import cc.allio.uno.data.orm.executor.interceptor.Interceptor;

import java.util.List;

/**
 * {@link CommandExecutor}实例加载器
 *
 * @author jiangwei
 * @date 2024/1/10 16:02
 * @since 1.1.6
 */
public interface ExecutorLoader {

    CommandExecutor load(List<Interceptor> interceptors);
}

