package cc.allio.uno.data.tx;

import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * 基于{@link org.springframework.transaction.support.DefaultTransactionDefinition}
 *
 * @author j.x
 * @since 1.1.7
 */
public class TxDefinitionImpl<T extends TxDefinitionImpl<T>> implements TxDefinition<T> {

    private int propagationBehavior = TransactionDefinition.PROPAGATION_REQUIRES_NEW;
    private int isolationLevel = TransactionDefinition.ISOLATION_DEFAULT;
    private int timeout = TransactionDefinition.TIMEOUT_DEFAULT;
    private String name = TxDefinitionImpl.class.getSimpleName();
    private boolean readonly = false;

    @Override
    public T propagationBehavior(int propagation) {
        this.propagationBehavior = propagation;
        return self();
    }

    @Override
    public T isolationLevel(int isolationLevel) {
        this.isolationLevel = isolationLevel;
        return self();
    }

    @Override
    public T timeout(int timeout) {
        this.timeout = timeout;
        return self();
    }

    @Override
    public T name(String name) {
        this.name = name;
        return self();
    }

    @Override
    public T readonly(boolean readonly) {
        this.readonly = readonly;
        return self();
    }

    @Override
    public TransactionDefinition getTransactionDefinition() {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setPropagationBehavior(propagationBehavior);
        transactionDefinition.setIsolationLevel(isolationLevel);
        transactionDefinition.setReadOnly(readonly);
        transactionDefinition.setName(name);
        transactionDefinition.setTimeout(timeout);
        return transactionDefinition;
    }
}
