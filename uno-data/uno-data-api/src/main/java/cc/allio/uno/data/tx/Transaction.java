package cc.allio.uno.data.tx;

/**
 * 事物的代码块
 *
 * @author jiangwei
 * @date 2024/2/11 11:15
 * @since 1.1.7
 */
@FunctionalInterface
public interface Transaction<R> {

    /**
     * 在事物之中执行代码逻辑
     *
     * @return 返回执行结果，如果存在任何异常将返回null
     * @throws Exception 代码块存在异常时抛出
     */
    R around() throws Exception;
}
