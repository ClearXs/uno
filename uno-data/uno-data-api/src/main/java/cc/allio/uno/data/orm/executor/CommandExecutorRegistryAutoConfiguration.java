package cc.allio.uno.data.orm.executor;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

@Configuration
@AutoConfigureAfter(ExecutorInitializerAutoConfiguration.class)
public class CommandExecutorRegistryAutoConfiguration {

    @Bean
    public CommandExecutorRegistry commandExecutorRegistry(ObjectProvider<List<ExecutorLoader>> loadProvider) {
        CommandExecutorRegistryImpl commandExecutorRegistry = new CommandExecutorRegistryImpl(loadProvider.getIfAvailable(Collections::emptyList));
        CommandExecutorFactory.setRegistry(commandExecutorRegistry);
        return commandExecutorRegistry;
    }
}
