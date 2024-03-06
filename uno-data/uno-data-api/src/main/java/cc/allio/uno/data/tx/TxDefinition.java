package cc.allio.uno.data.tx;

import cc.allio.uno.core.api.Self;
import org.springframework.transaction.TransactionDefinition;

/**
 * 事物定义
 *
 * @author jiangwei
 * @date 2024/2/11 11:17
 * @since 1.1.7
 */
public interface TxDefinition<T extends TxDefinition<T>> extends Self<T> {

    /**
     * 定义事物的传播级别
     *
     * @param propagation propagation
     * @return SELF
     * @see TransactionDefinition#getPropagationBehavior()
     */
    T propagationBehavior(int propagation);

    /**
     * 定义事物的隔离级别
     *
     * @param isolationLevel isolationLevel
     * @return SELF
     * @see TransactionDefinition#getIsolationLevel()
     */
    T isolationLevel(int isolationLevel);

    /**
     * 定义事物的超时时间
     *
     * @param timeout timeout
     * @return SELF
     * @see TransactionDefinition#getTimeout()
     */
    T timeout(int timeout);

    /**
     * 定义事物的只读
     *
     * @param readonly readonly
     * @return SELF
     * @see TransactionDefinition#isReadOnly()
     */
    T readonly(boolean readonly);

    /**
     * 定义事物的超时时间
     *
     * @param name name
     * @return SELF
     * @see TransactionDefinition#getName()
     */
    T name(String name);

    /**
     * 基于{@link #isolationLevel(int)}、{@link #propagationBehavior(int)}、{@link #timeout(int)}、{@link #name(String)}获取自定义化的事物配置
     * 或者采取默认的配置
     *
     * @return TransactionDefinition or default
     */
    TransactionDefinition getTransactionDefinition();

    default TxDefinition<T> withOther(TxDefinition<?> other) {
        TransactionDefinition transactionDefinition = other.getTransactionDefinition();
        return propagationBehavior(transactionDefinition.getPropagationBehavior())
                .isolationLevel(transactionDefinition.getIsolationLevel())
                .timeout(transactionDefinition.getTimeout())
                .readonly(transactionDefinition.isReadOnly())
                .name(transactionDefinition.getName());
    }

    /**
     * 获取默认的{@link TxDefinition}实例
     *
     * @return TxDefinition
     */
    static TxDefinition<?> withDefault() {
        return new TxDefinitionImpl<>();
    }
}
