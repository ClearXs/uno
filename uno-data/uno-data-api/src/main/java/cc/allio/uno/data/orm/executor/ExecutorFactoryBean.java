package cc.allio.uno.data.orm.executor;

import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import org.springframework.beans.factory.FactoryBean;

/**
 * 基于当前系统变量的{@link ExecutorKey#DSL_EXECUTOR_TYPE_KEY}获取对应的{@code CommandExecutor}
 *
 * @author jiangwei
 * @date 2024/1/10 18:10
 * @since 1.1.7
 */
public class ExecutorFactoryBean implements FactoryBean<CommandExecutor> {

    @Override
    public CommandExecutor getObject() throws Exception {
        ExecutorKey executorKey = ExecutorKey.getSystemExecutorKey();
        return CommandExecutorFactory.getDSLExecutor(executorKey);
    }

    @Override
    public Class<?> getObjectType() {
        return CommandExecutor.class;
    }
}
