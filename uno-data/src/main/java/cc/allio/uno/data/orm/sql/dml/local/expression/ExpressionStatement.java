package cc.allio.uno.data.orm.sql.dml.local.expression;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.data.orm.sql.SQLException;
import cc.allio.uno.data.orm.sql.Statement;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 使用{@link Expression}的语句
 *
 * @author jiangwei
 * @date 2023/1/6 13:11
 * @since 1.1.4
 */
public abstract class ExpressionStatement<T extends ExpressionStatement<T>> implements Statement<T> {

    protected final ExpressionGroup expressionGroup;
    protected final ExpressionContext expressionContext;

    protected ExpressionStatement(ExpressionContext expressionContext) {
        this.expressionGroup = new ExpressionGroup();
        this.expressionContext = expressionContext;
    }

    @Override
    public Collection<Expression> getExpressions() {
        return expressionGroup.getExpression();
    }

    @Override
    public String getSQL() throws SQLException {
        return expressionGroup.getSQL();
    }

    /**
     * 获取条件语句如：
     * <ul>
     *     <li>Select a , b -> a, b </li>
     *     <li>where a=1 -> a=1</li>
     * </ul>
     *
     * @return condition
     */
    @Override
    public String getCondition() {
        return expressionGroup.getExpression().stream()
                .filter(e -> !StatementExpression.class.isAssignableFrom(e.getClass()))
                .map(Expression::getSQL)
                .collect(Collectors.joining(StringPool.SPACE));
    }

    /**
     * 延迟把语句放入{@link ExpressionGroup}中
     *
     * @param expression expression实例
     * @param symbol     符号，如, AND OR...
     * @see ExpressionGroup#offer(Expression, String)
     */
    protected void lazyOffer(Expression expression, String symbol) {
        lazyOfferStatementSyntax();
        expressionGroup.offer(expression, symbol);
    }

    /**
     * 延迟把语句放入{@link ExpressionGroup}中
     *
     * @param expression expression实例
     * @param symbol     符号，如, AND OR...
     * @see ExpressionGroup#offerOne(Expression, String)
     */
    protected void lazyOfferOne(Expression expression, String symbol) {
        lazyOfferStatementSyntax();
        expressionGroup.offerOne(expression, symbol);
    }

    /**
     * 延迟提供语句语法
     */
    private void lazyOfferStatementSyntax() {
        String statementSyntax = getStatementSyntax();
        StatementExpression statementExpression = new StatementExpression(statementSyntax);
        if (expressionGroup.notContains(statementExpression)) {
            expressionGroup.offerOne(statementExpression, null);
        }
    }

    @Override
    public String toString() {
        return getSQL();
    }

    /**
     * 获取Statement语法
     *
     * @return Statement语法
     */
    protected abstract String getStatementSyntax();

}
