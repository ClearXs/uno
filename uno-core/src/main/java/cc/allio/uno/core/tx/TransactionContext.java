package cc.allio.uno.core.tx;

import cc.allio.uno.core.util.CoreBeanUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * 基于Spring编程式事物工具
 *
 * @author jiangwei
 * @date 2022/1/10 21:57
 * @since 1.0
 */
@Slf4j
public class TransactionContext {

    /**
     * 默认事物定义，事物传播行为与隔离级别
     */
    private static final DefaultTransactionDefinition DEFINITION = new DefaultTransactionDefinition();

    static {
        DEFINITION.setReadOnly(false);
        DEFINITION.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    /**
     * 基于Spring事物管理，执行事物代码，当执行过程中抛出异常将会捕获，并且回滚事物。
     *
     * @param transaction 可执行事物代码块，非空
     * @throws NoSuchBeanDefinitionException 在IOC容器中没有找到{@link PlatformTransactionManager}实例抛出
     */
    public static void execute(@NonNull Transaction transaction) {
        PlatformTransactionManager transactionManager = null;
        try {
            transactionManager = CoreBeanUtil.getBean(PlatformTransactionManager.class);
        } catch (NullPointerException e) {
            log.error("transactionManager is null", e);
        }
        if (transactionManager != null) {
            TransactionStatus status = transactionManager.getTransaction(DEFINITION);
            synchronized (TransactionContext.class) {
                try {
                    transaction.around();
                    transactionManager.commit(status);
                } catch (Exception e) {
                    log.error("transaction execute failed, data will be rollback", e);
                    transactionManager.rollback(status);
                }
            }
        }
    }

    /**
     * 定义包含事物的代码块
     */
    @FunctionalInterface
    public interface Transaction {

        /**
         * 将带着事物执行的代码块
         *
         * @throws Exception 代码块存在异常时抛出
         */
        void around() throws Exception;
    }
}
