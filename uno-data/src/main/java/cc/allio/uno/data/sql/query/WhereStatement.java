package cc.allio.uno.data.sql.query;

import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.data.sql.ExpressionColumnStatement;
import cc.allio.uno.data.sql.RuntimeColumn;
import cc.allio.uno.data.sql.SQLException;
import cc.allio.uno.data.sql.expression.*;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * SQL-Where条件，包含常用的ge、lte...如果没有指明使用{@link #and()}与{@link #or()}方法，所有的条件以'and'进行连接。
 * 当指明时，会把之前已经填写的条件以'AND'进行连接
 *
 * @author jiangwei
 * @date 2022/9/30 10:41
 * @since 1.1.0
 */
public class WhereStatement extends ExpressionColumnStatement<WhereStatement> implements Where<WhereStatement> {

    // 标识下一个sql是否进行逻辑语法控制
    private final AtomicBoolean nextLogicPredicate = new AtomicBoolean(false);
    // 逻辑连接词
    private String logicPredicate;
    // 默认连接的谓词
    private static final String DEFAULT_LOGIC_PREDICATE = AND;

    public WhereStatement(ExpressionContext expressionContext) {
        super(expressionContext);
    }

    /**
     * '>' 条件
     *
     * @param fieldName Java字段名称
     * @param value     比较数据值
     * @return WhereCondition对象
     */
    @Override
    public WhereStatement gt(String fieldName, Object value) {
        WhereColumn whereColumn = new WhereColumn(fieldName, new Object[]{value}, null);
        GTExpression expression = new GTExpression(whereColumn, expressionContext);
        logicConnect(expression, whereColumn);
        return self();
    }

    /**
     * '>=' 条件
     *
     * @param fieldName Java字段名称
     * @param value     比较数据值
     * @return WhereCondition对象
     */
    @Override
    public WhereStatement gte(String fieldName, Object value) {
        WhereColumn whereColumn = new WhereColumn(fieldName, new Object[]{value}, null);
        GTEExpression expression = new GTEExpression(whereColumn, expressionContext);
        logicConnect(expression, whereColumn);
        return self();
    }

    /**
     * '<' 条件
     *
     * @param fieldName Java字段名称
     * @param value     比较数据值
     * @return WhereCondition对象
     */
    @Override
    public WhereStatement lt(String fieldName, Object value) {
        WhereColumn whereColumn = new WhereColumn(fieldName, new Object[]{value}, null);
        LTExpression expression = new LTExpression(whereColumn, expressionContext);
        logicConnect(expression, whereColumn);
        return self();
    }

    /**
     * '<=' 条件
     *
     * @param fieldName Java字段名称
     * @param value     比较数据值
     * @return WhereCondition对象
     */
    @Override
    public WhereStatement lte(String fieldName, Object value) {
        WhereColumn whereColumn = new WhereColumn(fieldName, new Object[]{value}, null);
        LTEExpression expression = new LTEExpression(whereColumn, expressionContext);
        logicConnect(expression, whereColumn);
        return self();
    }

    /**
     * '=' 条件
     *
     * @param fieldName Java字段名称
     * @param value     比较数据值
     * @return WhereCondition对象
     */
    @Override
    public WhereStatement eq(String fieldName, Object value) {
        WhereColumn whereColumn = new WhereColumn(fieldName, new Object[]{value}, null);
        EQExpression expression = new EQExpression(whereColumn, expressionContext);
        logicConnect(expression, whereColumn);
        return self();
    }

    /**
     * is not null 条件
     *
     * @param fieldName Java字段名称
     * @return WhereCondition对象
     */
    @Override
    public WhereStatement notNull(String fieldName) {
        WhereColumn whereColumn = new WhereColumn(fieldName, null, null);
        NotNullExpression expression = new NotNullExpression(whereColumn, expressionContext);
        logicConnect(expression, whereColumn);
        return self();
    }

    @Override
    public WhereStatement isNull(String fieldName) {
        WhereColumn whereColumn = new WhereColumn(fieldName, null, null);
        IsNullExpression expression = new IsNullExpression(whereColumn, expressionContext);
        logicConnect(expression, whereColumn);
        return self();
    }

    /**
     * 'in'条件
     *
     * @param fieldName Java字段名称
     * @param values    数值数据
     * @return WhereCondition对象
     */
    @Override
    public WhereStatement in(String fieldName, Object[] values) {
        WhereColumn whereColumn = new WhereColumn(fieldName, values, null);
        INExpression expression = new INExpression(whereColumn, expressionContext);
        logicConnect(expression, whereColumn);
        return self();
    }

    /**
     * 'between' 条件
     *
     * @param fieldName Java字段名称
     * @param withValue between起始值
     * @param endValue  between结束值
     * @return WhereCondition对象
     */
    @Override
    public WhereStatement between(String fieldName, Object withValue, Object endValue) {
        WhereColumn whereColumn = new WhereColumn(fieldName, new Object[]{withValue, endValue}, null);
        BetweenExpression expression = new BetweenExpression(whereColumn, expressionContext, BetweenExpression.Strategy.IN);
        logicConnect(expression, whereColumn);
        return self();
    }

    @Override
    public WhereStatement notBetween(String fieldName, Object withValue, Object endValue) {
        WhereColumn whereColumn = new WhereColumn(fieldName, new Object[]{withValue, endValue}, null);
        BetweenExpression expression = new BetweenExpression(whereColumn, expressionContext, BetweenExpression.Strategy.NOT);
        logicConnect(expression, whereColumn);
        return self();
    }

    @Override
    public WhereStatement $like(String fieldName, Object value) {
        WhereColumn whereColumn = new WhereColumn(fieldName, new Object[]{value}, null);
        LikeExpression expression = new LikeExpression(whereColumn, expressionContext, LikeExpression.Strategy.LEFT);
        logicConnect(expression, whereColumn);
        return self();
    }

    @Override
    public WhereStatement like$(String fieldName, Object value) {
        WhereColumn whereColumn = new WhereColumn(fieldName, new Object[]{value}, null);
        LikeExpression expression = new LikeExpression(whereColumn, expressionContext, LikeExpression.Strategy.RIGHT);
        logicConnect(expression, whereColumn);
        return self();
    }

    @Override
    public WhereStatement $like$(String fieldName, Object value) {
        WhereColumn whereColumn = new WhereColumn(fieldName, new Object[]{value}, null);
        LikeExpression expression = new LikeExpression(whereColumn, expressionContext, LikeExpression.Strategy.ALL);
        logicConnect(expression, whereColumn);
        return self();
    }

    /**
     * 逻辑'与'控制
     *
     * @return WhereCondition对象
     */
    @Override
    public WhereStatement or() {
        nextLogicPredicate.set(true);
        logicPredicate = OR;
        return this;
    }

    /**
     * 逻辑'或'控制
     *
     * @return WhereCondition对象
     */
    @Override
    public WhereStatement and() {
        nextLogicPredicate.set(true);
        logicPredicate = AND;
        return this;
    }

    /**
     * 通过比较是否需要下一个语法进行逻辑连接。
     *
     * @param expression    expression实例
     * @param runtimeColumn column实例
     */
    public void logicConnect(Expression expression, RuntimeColumn runtimeColumn) {
        // 判断连接的谓词
        String predicate = DEFAULT_LOGIC_PREDICATE;
        if (StringUtils.isNotBlank(logicPredicate) && nextLogicPredicate.get()) {
            predicate = logicPredicate;
            // 重制
            logicPredicate = null;
            nextLogicPredicate.set(false);
        }
        lazyOffer(expression, predicate);
        addPrivatizationColumn(runtimeColumn);
    }

    @Override
    public void syntaxCheck() throws SQLException {

    }

    @Override
    public int order() {
        return WHERE_ORDER;
    }

    @Override
    protected String getStatementSyntax() {
        return WHERE;
    }
}
