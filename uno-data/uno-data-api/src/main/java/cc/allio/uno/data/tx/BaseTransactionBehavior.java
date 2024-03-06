package cc.allio.uno.data.tx;

import cc.allio.uno.core.exception.Exceptions;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;

/**
 * 基于{@link InternalTransactionBehavior}实现
 *
 * @author jiangwei
 * @date 2024/2/11 12:39
 * @since 1.1.7
 */
public abstract class BaseTransactionBehavior<T extends BaseTransactionBehavior<T>> implements TxDefinition<T> {

    protected final InternalTransactionBehavior internalTransactionBehavior;

    protected BaseTransactionBehavior(PlatformTransactionManager platformTransactionManager) {
        if (platformTransactionManager == null) {
            throw Exceptions.unchecked(new NullPointerException("platformTransactionManager must not null"));
        }
        this.internalTransactionBehavior = new InternalTransactionBehavior(platformTransactionManager);
    }

    @Override
    public T propagationBehavior(int propagation) {
        internalTransactionBehavior.propagationBehavior(propagation);
        return self();
    }

    @Override
    public T isolationLevel(int isolationLevel) {
        internalTransactionBehavior.isolationLevel(isolationLevel);
        return self();
    }

    @Override
    public T timeout(int timeout) {
        internalTransactionBehavior.timeout(timeout);
        return self();
    }

    @Override
    public T readonly(boolean readonly) {
        internalTransactionBehavior.readonly(readonly);
        return self();
    }

    @Override
    public T name(String name) {
        internalTransactionBehavior.name(name);
        return self();
    }

    @Override
    public TransactionDefinition getTransactionDefinition() {
        return internalTransactionBehavior.getTransactionDefinition();
    }
}
