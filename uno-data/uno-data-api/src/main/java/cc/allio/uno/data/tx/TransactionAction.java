package cc.allio.uno.data.tx;

/**
 * 定义事物动作
 *
 * @author jiangwei
 * @date 2024/2/11 12:34
 * @since 1.1.7
 */
public interface TransactionAction<R> {

    /**
     * 将带着事物执行的代码块
     *
     * @return 动作执行结果
     * @throws Exception 代码块存在异常时抛出
     */
    R around() throws Exception;
}
