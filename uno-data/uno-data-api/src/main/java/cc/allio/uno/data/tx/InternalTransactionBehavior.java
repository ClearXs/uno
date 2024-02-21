package cc.allio.uno.data.tx;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.TransactionSystemException;

@Slf4j
class InternalTransactionBehavior extends TxDefinitionImpl<InternalTransactionBehavior> {
    private final PlatformTransactionManager transactionManager;

    public InternalTransactionBehavior(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    /**
     * 基于Spring事物管理，执行事物代码，当执行过程中抛出异常将会捕获，并且回滚事物。
     *
     * @param voidTransaction 可执行事物代码块，非空
     * @throws NoSuchBeanDefinitionException 在IOC容器中没有找到{@link PlatformTransactionManager}实例抛出
     */
    public void execute(VoidTransactionAction voidTransaction) {
        if (transactionManager != null) {
            TransactionDefinition definition = getTransactionDefinition();
            TransactionStatus status = transactionManager.getTransaction(definition);
            synchronized (TransactionContext.class) {
                if (voidTransaction == null) {
                    throw new TransactionSystemException("transaction is empty");
                }
                try {
                    voidTransaction.around();
                    transactionManager.commit(status);
                } catch (Exception e) {
                    log.error("transaction execute failed, data will be rollback", e);
                    transactionManager.rollback(status);
                }
            }
        }
    }

    /**
     * 基于Spring事物管理，执行事物代码，当执行过程中抛出异常将会捕获，并且回滚事物。
     *
     * @param transaction 可执行事物代码块，非空
     * @throws NoSuchBeanDefinitionException 在IOC容器中没有找到{@link PlatformTransactionManager}实例抛出
     */
    public <R> R execute(Transaction<R> transaction) {
        if (transactionManager != null) {
            TransactionDefinition definition = getTransactionDefinition();
            TransactionStatus status = transactionManager.getTransaction(definition);
            synchronized (TransactionContext.class) {
                if (transaction == null) {
                    throw new TransactionSystemException("transaction is empty");
                }
                try {
                    R r = transaction.around();
                    transactionManager.commit(status);
                    return r;
                } catch (Exception e) {
                    log.error("transaction execute failed, data will be rollback", e);
                    transactionManager.rollback(status);
                }
            }
        }
        return null;
    }
}
