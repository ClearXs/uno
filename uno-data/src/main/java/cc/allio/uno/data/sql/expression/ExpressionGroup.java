package cc.allio.uno.data.sql.expression;

import cc.allio.uno.core.StringPool;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * ExpressionGroup queue
 * <p>  expression1 </p>
 * <p>  ,</p>
 * <p>  expression2</p>
 * <p>  ,</p>
 * <p>  expression3</p>
 *
 * @author jiangwei
 * @date 2023/1/6 11:53
 * @since 1.1.4
 */
public class ExpressionGroup implements Expression {

    private final LinkedList<Object> expressionSymbols = Lists.newLinkedList();

    /**
     * 向ExpressionGroup中提供数据支持
     * <p>在Select语句中添加,..</p>
     * <p>在Where语句中添加AND、OR...</p>
     *
     * @param expression 表达式实例
     * @param symbol     符号，如, AND OR...
     */
    public void offer(Expression expression, String symbol) {
        // 不是 single expression
        Object last;
        try {
            last = expressionSymbols.getLast();
        } catch (NoSuchElementException ex) {
            last = null;
        }
        // 上一个以及当前expression不是SingleExpression时才进行插入symbol数据
        if ((last != null && !SingleExpression.class.isAssignableFrom(last.getClass()))
                && !SingleExpression.class.isAssignableFrom(expression.getClass())) {
            expressionSymbols.add(symbol);
        }
        expressionSymbols.add(expression);
    }

    /**
     * 向ExpressionGroup中提供数据支持，只支持当前Expression类型存在于一种。通过比较Class对象
     *
     * @param expression 表达式实例
     * @param symbol     符号，如, AND OR...
     */
    public void offerOne(Expression expression, String symbol) {
        Iterator<Object> iterator = expressionSymbols.iterator();
        while (iterator.hasNext()) {
            Object inner = iterator.next();
            if (inner.getClass().isAssignableFrom(expression.getClass())) {
                iterator.remove();
                break;
            }
        }
        offer(expression, symbol);
    }

    /**
     * 向ExpressionGroup中提供数据支持，只支持一个symbol
     *
     * @param expression 表达式实例
     * @param symbol     符号，如, AND OR...
     */
    public void offerOneSymbol(Expression expression, String symbol) {
        Iterator<Object> iterator = expressionSymbols.iterator();
        while (iterator.hasNext()) {
            Object inner = iterator.next();
            if (inner.equals(symbol)) {
                iterator.remove();
                break;
            }
        }
        offer(expression, symbol);
    }

    /**
     * 向ExpressionGroup中提供数据支持
     *
     * @param symbol 符号
     */
    public void offerSymbol(String symbol) {
        expressionSymbols.add(symbol);
    }

    /**
     * 获取当前存储的{@link Expression}实例
     */
    public Collection<Expression> getExpression() {
        return expressionSymbols.stream()
                .filter(e -> Expression.class.isAssignableFrom(e.getClass()))
                .map(Expression.class::cast)
                .collect(Collectors.toList());
    }

    /**
     * 判断指定的元素是否包含在group中
     *
     * @param element 元素
     * @return true 存在 false 不存在
     */
    public boolean contains(Object element) {
        return expressionSymbols.contains(element);
    }

    /**
     * 判断指定元素是否不包含在group中
     *
     * @param element 元素
     * @return true 不存在 false 存在
     */
    public boolean notContains(Object element) {
        return !contains(element);
    }

    @Override
    public String getSQL() {
        return expressionSymbols.stream()
                .map(e -> {
                    if (e instanceof Expression) {
                        return ((Expression) e).getSQL();
                    }
                    return (String) e;
                })
                .collect(Collectors.joining(StringPool.SPACE));
    }
}
