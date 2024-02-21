package cc.allio.uno.data.test.model;

import cc.allio.uno.core.api.Self;
import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.type.DBType;
import com.google.common.collect.Lists;
import lombok.NonNull;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class Operators {

    private Operator<?> operator = null;
    private final List<Trigger> triggers = Lists.newArrayList();

    public Operators(Operator<?> operator) {
        this.operator = operator;
    }

    public static <T extends Operator<T>> OperatorFeature<T> feature(T operator) {
        return new OperatorFeature<>(operator);
    }

    public static void eachReset(@NonNull Operator<?> operator) {
        operator.reset();
    }

    public static void thenRest(Supplier<Operator<?>> supplier) {
        Operator<?> sqlOperator = supplier.get();
        if (sqlOperator != null) {
            sqlOperator.reset();
        }
    }

    public Operators then(Trigger trigger) {
        triggers.add(trigger);
        return this;
    }


    public void eachReset() {
        trigger(() -> {
            if (operator != null) {
                operator.reset();
            }
        });
    }

    private void trigger(Trigger parent) {
        for (Trigger trigger : triggers) {
            trigger.doAccept();
            parent.doAccept();
        }
    }

    public static class OperatorFeature<T extends Operator<T>> implements Self<OperatorFeature<T>> {

        private final T operator;
        private final List<Trigger> preTriggers;
        private final List<Trigger> postTriggers;

        public OperatorFeature(T operator) {
            this.operator = operator;
            this.preTriggers = Lists.newArrayList();
            this.postTriggers = Lists.newArrayList();
        }

        public OperatorFeature<T> dbType(DBType dbType) {
            this.preTriggers.add(() -> operator.setDBType(dbType));
            return self();
        }

        public OperatorFeature<T> thenReset() {
            this.postTriggers.add(operator::reset);
            return self();
        }

        public void trigger(Consumer<T> acceptor) {
            for (Trigger preTrigger : preTriggers) {
                preTrigger.doAccept();
            }
            acceptor.accept(operator);
            for (Trigger postTrigger : postTriggers) {
                postTrigger.doAccept();
            }
        }
    }

    public interface Trigger extends Consumer<Object> {

        @Override
        default void accept(Object o) {
            this.doAccept();
        }

        void doAccept();
    }
}
