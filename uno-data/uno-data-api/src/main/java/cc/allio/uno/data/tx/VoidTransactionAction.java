package cc.allio.uno.data.tx;

/**
 * 事物的代码块
 *
 * @author jiangwei
 * @date 2024/2/11 11:14
 * @since 1.1.6
 */
@FunctionalInterface
public interface VoidTransactionAction {

    /**
     * 在事物之中执行代码逻辑
     *
     * @throws Exception 代码块存在异常时抛出
     */
    void around() throws Exception;
}
