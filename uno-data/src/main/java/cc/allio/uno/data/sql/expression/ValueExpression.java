package cc.allio.uno.data.sql.expression;

import cc.allio.uno.core.OptionContext;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.context.ApplicationContext;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.*;
import java.util.function.Function;

/**
 * 对于eq lt lte gt...等等的语句都包需要包含值的作用。故单独创建该类，使实现类能够更好的使用值
 *
 * @author jiangwei
 * @date 2023/1/5 19:00
 * @since 1.1.4
 */
public abstract class ValueExpression implements Expression {

    protected final Object[] values;
    protected final ExpressionContext context;
    // 值暂位符
    protected final ValuePlaceholder valuePlaceholder;

    /**
     * 放入ValueExpression上下文对象
     *
     * @see ValuePlaceholder
     */
    public static final String CONTEXT_KEY = "context";

    protected ValueExpression(ExpressionContext context) {
        this(context, null);
    }

    protected ValueExpression(ExpressionContext context, Object[] values) {
        this(context, values, Collections.emptyMap());
    }

    protected ValueExpression(ExpressionContext context, Object[] values, Map<String, Object> runContext) {
        this.context = context;
        this.values = values;
        this.valuePlaceholder = new ValuePlaceholder(this);
        valuePlaceholder.putAll(runContext);
        valuePlaceholder.putAttribute(CONTEXT_KEY, context);
    }

    /**
     * 获取当前存放值数组的第一个值
     *
     * @return 第一个值
     */
    protected Optional<Object> getSingleValue() {
        if (values != null && values.length > 0) {
            return Optional.ofNullable(values[0]);
        }
        return Optional.empty();
    }

    /**
     * 获取当前存放值数组的第一个值
     *
     * @param defaultValue 如果没有的话的默认值
     * @return 第一个值 or 默认值
     */
    protected Object getSingleValue(Object defaultValue) {
        return getSingleValue().orElse(defaultValue);
    }

    /**
     * 获取当前存放值数组的第一个和第二个值
     *
     * @return double value
     * @throws NullPointerException 如果值是null的话则抛出
     */
    protected Optional<Tuple2<Object, Object>> getDoubleValue() {
        if (values != null && values.length > 1) {
            return Optional.of(Tuples.of(values[0], values[1]));
        }
        return Optional.empty();
    }

    /**
     * 获取当前存放值的数组
     *
     * @return values
     */
    protected Optional<Object[]> getValues() {
        return Optional.ofNullable(values);
    }

    /**
     * 子类实现，获取子类包含的集合对的结构，其中成对出现的Key为valuePlaceholder value为对应占位符的值.
     * <p>t1: Key t2: expression value</p>
     * <p>单值: eq lt lte gt gte...</p>
     * <p>双值:</p>
     * <p>多值: in</p>
     *
     * @return 集合结构
     */
    protected abstract Function<ValuePlaceholder, List<Tuple2<String, ExpressionValue>>> getValuePlaceholder();

    /**
     * ValuePlaceholder实例
     */
    static class ValuePlaceholder implements OptionContext {
        private final ValueExpression expression;
        private final List<Tuple2<String, ExpressionValue>> placeholderAndValues;
        private final Map<String, Object> context;

        public ValuePlaceholder(ValueExpression expression) {
            this.expression = expression;
            this.placeholderAndValues = Lists.newLinkedList();
            this.context = Maps.newHashMap();
        }

        /**
         * 根据指定的索引获取某个Value and ExpressionValue
         *
         * @param index 索引位置
         * @return t1 placeholder t2 value
         */
        public Tuple2<String, ExpressionValue> get(int index) {
            List<Tuple2<String, ExpressionValue>> lazy = getList();
            return lazy.get(index);
        }

        /**
         * 获取Value-ExpressionValue List
         *
         * @return List
         */
        public List<Tuple2<String, ExpressionValue>> getList() {
            // 延迟加载
            if (cc.allio.uno.core.util.Collections.isEmpty(placeholderAndValues)) {
                placeholderAndValues.addAll(
                        Optional.ofNullable(expression.getValuePlaceholder()).map(func -> func.apply(this)).orElse(Collections.emptyList())
                );
                for (Tuple2<String, ExpressionValue> pairs : placeholderAndValues) {
                    expression.context.getExpressionVariables().put(pairs.getT1(), pairs.getT2());
                }
            }
            return placeholderAndValues;
        }

        @Override
        public Optional<Object> get(String key) {
            return Optional.ofNullable(context.get(key));
        }

        @Override
        public void putAttribute(String key, Object obj) {
            context.put(key, obj);
        }

        @Override
        public Optional<ApplicationContext> getApplicationContext() {
            return Optional.empty();
        }
    }
}
