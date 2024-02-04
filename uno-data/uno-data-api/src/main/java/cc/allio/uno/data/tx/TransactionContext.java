package cc.allio.uno.data.tx;

import cc.allio.uno.data.orm.dsl.Self;
import com.google.common.collect.Lists;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;

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

    private static PlatformTransactionManager transactionManager;

    public TransactionContext(PlatformTransactionManager transactionManager) {
        TransactionContext.transactionManager = transactionManager;
    }

    /**
     * 基于Spring事物管理，执行事物代码，当执行过程中抛出异常将会捕获，并且回滚事物。
     *
     * @param transaction 可执行事物代码块，非空
     * @throws NoSuchBeanDefinitionException 在IOC容器中没有找到{@link PlatformTransactionManager}实例抛出
     */
    public static void execute(@NonNull Transaction transaction) {
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
     * 开启一个新的事物
     */
    public static TransactionBehavior open() {
        if (transactionManager == null) {
            throw new IllegalArgumentException("transactionManager is null, please set TransactionManger through out TransactionContext.transactionManager = xxx");
        }
        return new TransactionBehavior();
    }

    /**
     * 事物行为
     */
    public static class TransactionBehavior implements Self<TransactionBehavior> {

        private final List<TransactionAction> actions = Lists.newArrayList();

        /**
         * 提供事物行为动作
         *
         * @param action action
         * @return TransactionBehavior
         */
        public TransactionBehavior then(TransactionAction action) {
            this.actions.add(action);
            return self();
        }

        /**
         * 提交事物行为
         */
        public void commit() {
            TransactionContext.execute(() -> actions.forEach(TransactionAction::doAccept));
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
