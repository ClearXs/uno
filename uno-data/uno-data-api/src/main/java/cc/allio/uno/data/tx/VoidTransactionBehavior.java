package cc.allio.uno.data.tx;

import cc.allio.uno.core.function.VoidConsumer;
import com.google.common.collect.Lists;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

/**
 * void transaction behavior
 *
 * @author j.x
 * @date 2024/2/11 12:37
 * @since 1.1.7
 */
public class VoidTransactionBehavior extends BaseTransactionBehavior<VoidTransactionBehavior> {

    private final List<VoidConsumer> actions = Lists.newArrayList();

    public VoidTransactionBehavior(PlatformTransactionManager platformTransactionManager) {
        super(platformTransactionManager);
    }

    /**
     * 提供事物行为动作
     *
     * @param action action
     * @return VoidTransactionBehavior
     */
    public VoidTransactionBehavior then(VoidConsumer action) {
        this.actions.add(action);
        return self();
    }

    /**
     * 提交事物行为
     */
    public void commit() {
        internalTransactionBehavior.execute(() -> actions.forEach(VoidConsumer::doAccept));
    }
}
