package cc.allio.uno.data.sql;

import cc.allio.uno.data.sql.expression.Expression;
import cc.allio.uno.data.sql.expression.ExpressionContext;
import cc.allio.uno.data.sql.expression.StatementExpression;
import cc.allio.uno.data.sql.expression.ExpressionGroup;

import java.util.Collection;

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
