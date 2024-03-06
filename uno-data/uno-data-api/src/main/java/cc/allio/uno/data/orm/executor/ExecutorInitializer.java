package cc.allio.uno.data.orm.executor;

import cc.allio.uno.data.orm.executor.interceptor.Interceptor;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

/**
 * 初始化{@link CommandExecutor}
 *
 * @author jiangwei
 * @date 2024/1/10 16:04
 * @since 1.1.7
 */
public final class ExecutorInitializer implements InitializingBean {

    private final List<ExecutorLoader> loaders;
    private final List<Interceptor> interceptors;

    public ExecutorInitializer(List<ExecutorLoader> loaders, List<Interceptor> interceptors) {
        this.loaders = loaders;
        this.interceptors = interceptors;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        for (ExecutorLoader loader : loaders) {
            CommandExecutor commandExecutor = loader.load(interceptors);
            CommandExecutorFactory.register(commandExecutor);
        }
    }
}
