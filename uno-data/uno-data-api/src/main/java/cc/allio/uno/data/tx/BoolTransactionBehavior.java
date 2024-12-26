package cc.allio.uno.data.tx;

import cc.allio.uno.core.function.lambda.MethodVoidPredicate;
import com.google.common.collect.Lists;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;
import java.util.stream.Stream;

/**
 * bool
 *
 * @author j.x
 * @since 1.1.7
 */
public class BoolTransactionBehavior extends BaseTransactionBehavior<BoolTransactionBehavior> {

    private final List<MethodVoidPredicate> predicates = Lists.newArrayList();
    private final Mode mode;

    public BoolTransactionBehavior(PlatformTransactionManager platformTransactionManager) {
        this(platformTransactionManager, Mode.ALL_MATCH);
    }

    public BoolTransactionBehavior(PlatformTransactionManager platformTransactionManager, Mode mode) {
        super(platformTransactionManager);
        this.mode = mode;
    }

    /**
     * 提供事物行为动作
     *
     * @param predicate predicate
     * @return BoolTransactionBehavior
     */
    public BoolTransactionBehavior then(MethodVoidPredicate predicate) {
        this.predicates.add(predicate);
        return self();
    }

    /**
     * 提交事物行为
     */
    public boolean commit() {
        return TransactionContext.execute(() -> {
            Stream<MethodVoidPredicate> predicatesStreams = predicates.stream();
            if (Mode.ALL_MATCH == mode) {
                return predicatesStreams.allMatch(MethodVoidPredicate::test);
            } else if (Mode.ANY_MATCH == mode) {
                return predicatesStreams.anyMatch(MethodVoidPredicate::test);
            } else if (Mode.NONE_MATCH == mode) {
                return predicatesStreams.noneMatch(MethodVoidPredicate::test);
            }
            return false;
        });
    }


    public enum Mode {
        // 所有进行匹配，如果其中一个返回false则不在进行后续动作
        ALL_MATCH,
        // 任意匹配
        ANY_MATCH,
        // 不匹配任意一个
        NONE_MATCH;
    }

}
