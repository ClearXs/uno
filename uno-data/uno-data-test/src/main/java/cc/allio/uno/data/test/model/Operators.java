package cc.allio.uno.data.test.model;

import cc.allio.uno.data.orm.dsl.Operator;
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

    public interface Trigger extends Consumer<Object> {

        @Override
        default void accept(Object o) {
            this.doAccept();
        }

        void doAccept();
    }
}
