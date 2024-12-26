package cc.allio.uno.data.tx;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.transaction.*;

import java.util.function.UnaryOperator;

/**
 * 基于Spring编程式事物工具
 *
 * @author j.x
 * @since 1.0
 */
@Slf4j
public final class TransactionContext {

    private static PlatformTransactionManager transactionManager;

    public TransactionContext(PlatformTransactionManager transactionManager) {
        TransactionContext.transactionManager = transactionManager;
    }

    /**
     * @see #execute(VoidTransactionAction, TxDefinition)
     */
    public static void execute(VoidTransactionAction voidTransaction) {
        execute(voidTransaction, f -> f);
    }

    /**
     * @see #execute(VoidTransactionAction, TxDefinition)
     */
    public static void execute(VoidTransactionAction voidTransaction, UnaryOperator<TxDefinition<?>> func) {
        execute(voidTransaction, func.apply(TxDefinition.withDefault()));
    }

    /**
     * 基于Spring事物管理，执行事物代码，当执行过程中抛出异常将会捕获，并且回滚事物。
     *
     * @param voidTransaction 可执行事物代码块，非空
     * @param txDefinition    事物定义
     * @throws NoSuchBeanDefinitionException 在IOC容器中没有找到{@link PlatformTransactionManager}实例抛出
     */
    public static void execute(VoidTransactionAction voidTransaction, TxDefinition<?> txDefinition) {
        InternalTransactionBehavior transactionBehavior = new InternalTransactionBehavior(transactionManager);
        transactionBehavior.withOther(txDefinition);
        transactionBehavior.execute(voidTransaction);
    }

    /**
     * @see #execute(Transaction, TxDefinition)
     */
    public static <R> R execute(Transaction<R> transaction) {
        return execute(transaction, f -> f);
    }

    /**
     * @see #execute(Transaction, TxDefinition)
     */
    public static <R> R execute(Transaction<R> transaction, UnaryOperator<TxDefinition<?>> func) {
        return execute(transaction, func.apply(TxDefinition.withDefault()));
    }

    /**
     * 基于Spring事物管理，执行事物代码，当执行过程中抛出异常将会捕获，并且回滚事物。
     *
     * @param transaction  可执行事物代码块，非空
     * @param txDefinition 事物定义
     * @throws NoSuchBeanDefinitionException 在IOC容器中没有找到{@link PlatformTransactionManager}实例抛出
     */
    public static <R> R execute(Transaction<R> transaction, TxDefinition<?> txDefinition) {
        InternalTransactionBehavior transactionBehavior = new InternalTransactionBehavior(transactionManager);
        transactionBehavior.withOther(txDefinition);
        return transactionBehavior.execute(transaction);
    }

    /**
     * 开启一个新的事物，该方法将返回{@link VoidTransactionBehavior}对象
     */
    public static VoidTransactionBehavior open() {
        if (transactionManager == null) {
            throw new IllegalArgumentException("transactionManager is null, please set TransactionManger through out TransactionContext.transactionManager = xxx");
        }
        return new VoidTransactionBehavior(transactionManager);
    }

    /**
     * 开启一个新的事物，该方法将返回{@link BoolTransactionBehavior}对象。
     */
    public static BoolTransactionBehavior boolOpen() {
        if (transactionManager == null) {
            throw new IllegalArgumentException("transactionManager is null, please set TransactionManger through out TransactionContext.transactionManager = xxx");
        }
        return new BoolTransactionBehavior(transactionManager);
    }


    /**
     * 开启一个新的事物，该方法将返回{@link BoolTransactionBehavior}对象。
     */
    public static BoolTransactionBehavior allMatchOpen() {
        if (transactionManager == null) {
            throw new IllegalArgumentException("transactionManager is null, please set TransactionManger through out TransactionContext.transactionManager = xxx");
        }
        return new BoolTransactionBehavior(transactionManager, BoolTransactionBehavior.Mode.ALL_MATCH);
    }

    public static BoolTransactionBehavior anyMatchOpen() {
        if (transactionManager == null) {
            throw new IllegalArgumentException("transactionManager is null, please set TransactionManger through out TransactionContext.transactionManager = xxx");
        }
        return new BoolTransactionBehavior(transactionManager, BoolTransactionBehavior.Mode.ANY_MATCH);
    }

    public static BoolTransactionBehavior noneMatchOpen() {
        if (transactionManager == null) {
            throw new IllegalArgumentException("transactionManager is null, please set TransactionManger through out TransactionContext.transactionManager = xxx");
        }
        return new BoolTransactionBehavior(transactionManager, BoolTransactionBehavior.Mode.NONE_MATCH);
    }
}
