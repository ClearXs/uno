package cc.allio.uno.data.orm.executor;

import org.springframework.beans.factory.FactoryBean;

/**
 * 基于当前系统变量的{@link ExecutorKey#SQL_EXECUTOR_TYPE_KEY}获取对应的{@code CommandExecutor}
 *
 * @author jiangwei
 * @date 2024/1/10 18:10
 * @since 1.1.6
 */
public class ExecutorFactoryBean implements FactoryBean<CommandExecutor> {

    @Override
    public CommandExecutor getObject() throws Exception {
        ExecutorKey executorKey = ExecutorKey.getSystemExecutorKey();
        return CommandExecutorFactory.getSQLExecutor(executorKey);
    }

    @Override
    public Class<?> getObjectType() {
        return CommandExecutor.class;
    }
}
