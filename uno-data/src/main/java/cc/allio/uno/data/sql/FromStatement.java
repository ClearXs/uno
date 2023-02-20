package cc.allio.uno.data.sql;

import cc.allio.uno.core.util.ClassUtil;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.data.sql.expression.Expression;
import cc.allio.uno.data.sql.expression.ExpressionContext;
import cc.allio.uno.data.sql.expression.FromExpression;

import java.util.Collection;

/**
 * from statement contains
 * <p>
 * from xxx
 * </p>
 * <p>
 * left join xxx
 * </p>
 *
 * @author jiangwei
 * @date 2023/1/9 18:44
 * @since 1.1.4
 */
public class FromStatement extends ExpressionStatement<FromStatement> implements From<FromStatement> {

    public FromStatement(ExpressionContext expressionContext) {
        super(expressionContext);
    }

    @Override
    public FromStatement from(Class<?> tableEntity) throws SQLException {
        javax.persistence.Table table = ClassUtil.getAnnotation(tableEntity, javax.persistence.Table.class);
        if (table == null) {
            throw new SQLException("Specifies @Table annotation for entity");
        }
        String tableName = table.name();
        if (StringUtils.isBlank(tableName)) {
            tableName = tableEntity.getSimpleName();
        }
        FromExpression fromExpression = new FromExpression(new Table(StringUtils.camelToUnderline(tableName)), expressionContext);
        lazyOfferOne(fromExpression, null);
        return self();
    }

    @Override
    public FromStatement from(String table) throws SQLException {
        FromExpression fromExpression = new FromExpression(new Table(StringUtils.camelToUnderline(table)), expressionContext);
        lazyOfferOne(fromExpression, null);
        return self();
    }

    @Override
    public String getSQL() throws SQLException {
        return expressionGroup.getSQL();
    }

    @Override
    protected String getStatementSyntax() {
        return FROM;
    }

    @Override
    public Collection<Expression> getExpressions() {
        return expressionGroup.getExpression();
    }

    @Override
    public void syntaxCheck() throws SQLException {

    }

    @Override
    public int order() {
        return FROM_ORDER;
    }
}
