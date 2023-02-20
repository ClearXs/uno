package cc.allio.uno.data.sql.query;

import cc.allio.uno.data.sql.Alias;
import cc.allio.uno.data.sql.ExpressionColumnStatement;
import cc.allio.uno.data.sql.SQLException;
import cc.allio.uno.data.sql.word.Distinct;
import cc.allio.uno.data.sql.expression.*;
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
    public SelectStatement function(String syntax, String fieldName, String alias, Distinct distinct) {
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
    public int order() {
        return SELECT_ORDER;
    }

    @Override
    protected String getStatementSyntax() {
        return SELECT;
    }
}
