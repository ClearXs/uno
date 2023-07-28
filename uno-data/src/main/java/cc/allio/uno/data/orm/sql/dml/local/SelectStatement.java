package cc.allio.uno.data.orm.sql.dml.local;

import cc.allio.uno.data.orm.sql.word.Distinct;
import cc.allio.uno.data.orm.sql.Alias;
import cc.allio.uno.data.orm.sql.dml.local.expression.ExpressionColumnStatement;
import cc.allio.uno.data.orm.sql.dml.local.expression.ExpressionContext;
import cc.allio.uno.data.orm.sql.SQLException;
import cc.allio.uno.data.orm.sql.dml.Select;
import cc.allio.uno.data.orm.sql.dml.local.expression.DistinctExpression;
import cc.allio.uno.data.orm.sql.dml.local.expression.FunctionExpression;
import cc.allio.uno.data.orm.sql.dml.local.expression.PlainExpression;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

/**
 * SQL Select语句
 *
 * @author jiangwei
 * @date 2022/9/30 13:45
 * @since 1.1.0
 */
public class SelectStatement extends ExpressionColumnStatement<SelectStatement> implements Select<SelectStatement> {

    public SelectStatement(ExpressionContext expressionContext) {
        super(expressionContext);
    }

    public SelectStatement select(String fieldName) {
        SelectColumn selectColumn = new SelectColumn(StringUtils.camelToUnderline(fieldName), null, null);
        PlainExpression plainExpression = new PlainExpression(selectColumn, expressionContext);
        lazyOffer(plainExpression, StringPool.COMMA);
        addPrivatizationColumn(selectColumn);
        return this;
    }

    @Override
    public SelectStatement select(String fieldName, String alias) {
        SelectColumn selectColumn = new SelectColumn(StringUtils.camelToUnderline(fieldName), null, null);
        PlainExpression plainExpression = new PlainExpression(selectColumn, expressionContext);
        plainExpression.alias(alias);
        lazyOffer(plainExpression, StringPool.COMMA);
        addPrivatizationColumn(selectColumn);
        return this;
    }

    @Override
    public SelectStatement select(String[] fieldNames) {
        return select(Lists.newArrayList(fieldNames));
    }

    @Override
    public SelectStatement select(Collection<String> fieldNames) {
        for (String name : fieldNames) {
            SelectColumn selectColumn = new SelectColumn(StringUtils.camelToUnderline(name), null, null);
            PlainExpression plainExpression = new PlainExpression(selectColumn, expressionContext);
            lazyOffer(plainExpression, StringPool.COMMA);
            addPrivatizationColumn(selectColumn);
        }
        return self();
    }

    @Override
    public SelectStatement distinct() {
        lazyOfferOne(new DistinctExpression(null, null), null);
        return self();
    }

    @Override
    public SelectStatement distinctOn(String fieldName, String alias) {
        SelectColumn selectColumn = new SelectColumn(StringUtils.camelToUnderline(fieldName), null, null);
        PlainExpression plainExpression = new PlainExpression(selectColumn, expressionContext);
        lazyOffer(new DistinctExpression(plainExpression, alias), null);
        return self();
    }

    @Override
    public SelectStatement aggregate(String syntax, String fieldName, String alias, Distinct distinct) {
        List<Object> arguments = Lists.newArrayList();
        if (StringUtils.isNotBlank(fieldName)) {
            SelectColumn column = new SelectColumn(StringUtils.camelToUnderline(fieldName), null, null);
            arguments.add(column);
        }
        if (StringUtils.isNotBlank(alias)) {
            Alias aliasTo = new Alias(alias);
            arguments.add(aliasTo);
        }
        if (distinct != null) {
            arguments.add(distinct);
        }
        lazyOffer(new FunctionExpression(expressionContext, syntax, arguments.toArray()), null);
        return self();
    }

    @Override
    public void syntaxCheck() throws SQLException {

    }

    @Override
    protected String getStatementSyntax() {
        return SELECT;
    }
}
