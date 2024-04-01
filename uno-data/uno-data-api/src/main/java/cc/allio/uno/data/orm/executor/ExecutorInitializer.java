package cc.allio.uno.data.orm.executor;

import cc.allio.uno.data.orm.executor.interceptor.Interceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

/**
 * 初始化{@link CommandExecutor}
 *
 * @author j.x
 * @date 2024/1/10 16:04
 * @since 1.1.7
 */
@Slf4j
public final class ExecutorInitializer implements InitializingBean {

    private final List<CommandExecutorLoader<? extends AggregateCommandExecutor>> loaders;
    private final List<Interceptor> interceptors;

    public ExecutorInitializer(List<CommandExecutorLoader<? extends AggregateCommandExecutor>> loaders, List<Interceptor> interceptors) {
        this.loaders = loaders;
        this.interceptors = interceptors;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        for (CommandExecutorLoader<? extends AggregateCommandExecutor> loader : loaders) {
            AggregateCommandExecutor commandExecutor = loader.load(interceptors);
            if (log.isDebugEnabled()) {
                log.debug("load command executor by {}, the result is {}", loader.getClass().getSimpleName(), commandExecutor);
            }
            if (commandExecutor != null) {
                CommandExecutorFactory.register(commandExecutor);
            }
        }
    }
}
