package cc.allio.uno.data.orm.executor.internal;

import cc.allio.uno.core.spi.ClassPathServiceLoader;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import com.google.auto.service.AutoService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * scan include annotation {@link cc.allio.uno.data.orm.executor.CommandExecutor.Group} the {@link InnerCommandExecutor}
 *
 * @author j.x
 * @date 2024/3/15 08:36
 * @since 1.1.7
 */
public final class SPIInnerCommandScanner {

    private static final Set<Class<? extends InnerCommandExecutor>> INNER_COMMAND_EXECUTOR_CLASSES = Sets.newHashSet();
    private final ExecutorKey executorKey;

    static {
        INNER_COMMAND_EXECUTOR_CLASSES.add(CTOInnerCommandExecutor.class);
        INNER_COMMAND_EXECUTOR_CLASSES.add(DOInnerCommandExecutor.class);
        INNER_COMMAND_EXECUTOR_CLASSES.add(DTOInnerCommandExecutor.class);
        INNER_COMMAND_EXECUTOR_CLASSES.add(ETOInnerCommandExecutor.class);
        INNER_COMMAND_EXECUTOR_CLASSES.add(IOInnerCommandExecutor.class);
        INNER_COMMAND_EXECUTOR_CLASSES.add(QOInnerCommandExecutor.class);
        INNER_COMMAND_EXECUTOR_CLASSES.add(SCOInnerCommandExecutor.class);
        INNER_COMMAND_EXECUTOR_CLASSES.add(UOInnerCommandExecutor.class);
        INNER_COMMAND_EXECUTOR_CLASSES.add(ATOInnerCommandExecutor.class);
        INNER_COMMAND_EXECUTOR_CLASSES.add(STInnerCommandExecutor.class);
    }

    public SPIInnerCommandScanner(ExecutorKey executorKey) {
        this.executorKey = executorKey;
    }

    /**
     * base on google {@link AutoService} method, from META-INF acquire {@link InnerCommandExecutor} and create {@link InnerCommandExecutorManager}
     *
     * @param args crate {@link InnerCommandExecutor} arguments
     * @return InnerCommandExecutorManager instance
     */
    public InnerCommandExecutorManager scan(Object... args) {
        InnerCommandExecutorManager manager = new InnerCommandExecutorManager();
        // combine
        List<? extends InnerCommandExecutor> innerCommandExecutors = INNER_COMMAND_EXECUTOR_CLASSES.stream()
                .flatMap(innerCommandExecutorClass -> {
                    ClassPathServiceLoader<? extends InnerCommandExecutor> load = ClassPathServiceLoader.load(innerCommandExecutorClass, args);
                    return Lists.newArrayList(load).stream();
                })
                // filter not ExecutorKey InnerCommandExecutor
                .filter(provider -> {
                    Class<? extends InnerCommandExecutor> type = provider.type();
                    CommandExecutor.Group group = AnnotationUtils.findAnnotation(type, CommandExecutor.Group.class);
                    return group != null && group.value().equals(executorKey.key());
                })
                .map(ClassPathServiceLoader.Provider::get)
                .collect(Collectors.toList());

        // set
        for (InnerCommandExecutor innerCommandExecutor : innerCommandExecutors) {
            manager.set(innerCommandExecutor.getRealityOperatorType(), innerCommandExecutor);
        }
        return manager;
    }
}
