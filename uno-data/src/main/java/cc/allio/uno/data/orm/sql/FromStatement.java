package cc.allio.uno.data.orm.sql;

import cc.allio.uno.core.util.ClassUtils;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.data.orm.sql.dml.local.expression.Expression;
import cc.allio.uno.data.orm.sql.dml.local.expression.ExpressionContext;
import cc.allio.uno.data.orm.sql.dml.local.expression.ExpressionStatement;
import cc.allio.uno.data.orm.sql.dml.local.expression.FromExpression;

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
 * @deprecated 1.1.4版本删除
 */
@Deprecated
public class FromStatement extends ExpressionStatement<FromStatement> implements From<FromStatement> {

    public FromStatement(ExpressionContext expressionContext) {
        super(expressionContext);
    }

    @Override
    public FromStatement from(Class<?> tableEntity) throws SQLException {
        javax.persistence.Table table = ClassUtils.getAnnotation(tableEntity, javax.persistence.Table.class);
        if (table == null) {
            throw new SQLException("Specifies @Table annotation for entity");
        }
        String tableName = table.name();
        if (StringUtils.isBlank(tableName)) {
            tableName = tableEntity.getSimpleName();
        }
        FromExpression fromExpression = new FromExpression(Table.of(StringUtils.camelToUnderline(tableName)), expressionContext);
        lazyOfferOne(fromExpression, null);
        return self();
    }

    @Override
    public FromStatement from(String table) throws SQLException {
        FromExpression fromExpression = new FromExpression(Table.of(StringUtils.camelToUnderline(table)), expressionContext);
        lazyOfferOne(fromExpression, null);
        return self();
    }

    @Override
    public FromStatement from(String table, String alias) {
        return null;
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
