package cc.allio.uno.data.orm.executor;

/**
 * 实现方获取动态获取{@link CommandExecutor}实例。
 * <p>
 * 该包提供相关API用于具体的实现继承，由具体的executor提供如何动态获取方法。
 * </p>
 * <p>
 * 核心思路是采用编程式Aspect创建代理类
 * </p>
 * <p>
 * <b>该接口的实现类必须是Spring Bean</b>
 * </p>
 *
 * @author j.x
 * @since 1.1.7
 */
public interface CommandExecutorAware<E extends AggregateCommandExecutor> {

    /**
     * 获取CommandExecutor实例
     *
     * @return CommandExecutor
     */
    default E getExecutor() {
        return null;
    }
}
