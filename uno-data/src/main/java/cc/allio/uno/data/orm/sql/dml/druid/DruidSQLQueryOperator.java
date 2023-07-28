package cc.allio.uno.data.orm.sql.dml.druid;

import cc.allio.uno.data.orm.dialect.func.Func;
import cc.allio.uno.data.orm.sql.*;
import cc.allio.uno.data.orm.sql.SQLName;
import cc.allio.uno.data.orm.sql.dml.local.OrderCondition;
import cc.allio.uno.data.orm.sql.word.Distinct;
import cc.allio.uno.data.orm.type.DBType;
import cc.allio.uno.data.orm.type.druid.DruidDbTypeAdapter;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.*;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.visitor.ParameterizedOutputVisitorUtils;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.core.type.Types;
import cc.allio.uno.data.orm.sql.dml.SQLQueryOperator;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Druid Query Operator
 *
 * @author jiangwei
 * @date 2023/4/12 23:08
 * @since 1.1.4
 */
public class DruidSQLQueryOperator extends SQLPrepareOperatorImpl<SQLQueryOperator> implements SQLQueryOperator {

    private final DbType druidDbType;
    private final DruidTokenOperatorAdapter tokenOperatorAdapter;

    private SQLSelectQueryBlock selectQuery;
    private SQLTableSource tableSource;

    // order by
    private SQLOrderBy orderBy;
    // group by
    private SQLSelectGroupByClause groupBy;
    // limit by
    private SQLLimit sqlLimit;

    public DruidSQLQueryOperator(DBType dbType) {
        super();
        this.tokenOperatorAdapter = new DruidTokenOperatorAdapter();
        this.druidDbType = DruidDbTypeAdapter.getInstance().get(dbType);
        this.selectQuery = new SQLSelectQueryBlock();
        selectQuery.setDbType(druidDbType);
    }

    @Override
    public String getSQL() {
        List<Object> values = getPrepareValues().stream().map(PrepareValue::getValue).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(values)) {
            return getPrepareSQL();
        }
        return ParameterizedOutputVisitorUtils.restore(
                getPrepareSQL(),
                druidDbType,
                getPrepareValues().stream().map(PrepareValue::getValue).collect(Collectors.toList()));
    }

    @Override
    public SQLQueryOperator parse(String sql) {
        return null;
    }

    @Override
    public void reset() {
        super.reset();
        this.selectQuery = new SQLSelectQueryBlock();
        selectQuery.setDbType(druidDbType);
        this.tableSource = null;
        this.orderBy = null;
        this.groupBy = null;
        this.sqlLimit = null;
    }

    @Override
    public SQLQueryOperator select(SQLName sqlName) {
        SQLSelectItem sqlSelectItem;
        if (StringPool.ASTERISK.equals(sqlName.format())) {
            sqlSelectItem = new SQLSelectItem(new SQLAllColumnExpr());
        } else {
            sqlSelectItem = new SQLSelectItem(new SQLIdentifierExpr(sqlName.format()));
        }
        selectQuery.addSelectItem(sqlSelectItem);
        return self();
    }

    @Override
    public SQLQueryOperator select(SQLName sqlName, String alias) {
        SQLSelectItem sqlSelectItem = new SQLSelectItem(new SQLIdentifierExpr(sqlName.format()), alias);
        selectQuery.addSelectItem(sqlSelectItem);
        return self();
    }

    @Override
    public SQLQueryOperator selects(Collection<SQLName> sqlNames) {
        for (SQLName sqlName : sqlNames) {
            SQLSelectItem sqlSelectItem = new SQLSelectItem(new SQLIdentifierExpr(sqlName.format()));
            selectQuery.addSelectItem(sqlSelectItem);
        }
        return self();
    }

    @Override
    public SQLQueryOperator distinct() {
        SQLSelectItem sqlSelectItem = new SQLSelectItem(new SQLAggregateExpr("", SQLAggregateOption.DISTINCT));
        selectQuery.addSelectItem(sqlSelectItem);
        return self();
    }

    @Override
    public SQLQueryOperator distinctOn(SQLName sqlName, String alias) {
        SQLSelectItem sqlSelectItem = new SQLSelectItem(new SQLAggregateExpr(sqlName.format(), SQLAggregateOption.DISTINCT));
        selectQuery.addSelectItem(sqlSelectItem);
        return self();
    }

    @Override
    public SQLQueryOperator aggregate(Func syntax, SQLName sqlName, String alias, Distinct distinct) {
        SQLSelectItem sqlSelectItem = new SQLSelectItem();
        SQLAggregateExpr sqlAggregateExpr = new SQLAggregateExpr(syntax.getName());
        if (distinct != null) {
            sqlAggregateExpr.setOption(SQLAggregateOption.DISTINCT);
        }
        sqlAggregateExpr.addArgument(new SQLIdentifierExpr(sqlName.format()));
        sqlSelectItem.setExpr(sqlAggregateExpr);
        selectQuery.addSelectItem(sqlSelectItem);
        return self();
    }

    @Override
    public SQLQueryOperator from(Table table) {
        this.tableSource = new SQLExprTableSource(new SQLIdentifierExpr(table.getName().getName()), table.getAlias());
        return self();
    }

    @Override
    public SQLQueryOperator from(SQLQueryOperator fromTable, String alias) {
        String fromSQL = fromTable.getSQL();
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement(fromSQL, druidDbType, true);
        SQLSubqueryTableSource from = new SQLSubqueryTableSource((SQLSelect) sqlStatement);
        from.setAlias(alias);
        this.tableSource = from;
        return self();
    }

    @Override
    public SQLQueryOperator join(Table left, JoinType joinType, Table right, SQLBinaryCondition condition) {
        // 构建连接关系
        SQLExprTableSource rightSource = new SQLExprTableSource(new SQLIdentifierExpr(right.getName().format()), right.getAlias());
        SQLBinaryOperator operator = tokenOperatorAdapter.get(condition.getSyntax());
        SQLBinaryOpExpr opExpr = new SQLBinaryOpExpr(new SQLIdentifierExpr(condition.getLeft()), operator, new SQLIdentifierExpr(condition.getRight()));
        // 判断是否已经包含连接关系，如果则组建复合关系
        if (tableSource != null && tableSource instanceof SQLJoinTableSource) {
            SQLJoinTableSource combineTableSource = new SQLJoinTableSource();
            tableSource.setAlias(left.getAlias());
            combineTableSource.setLeft(tableSource);
            combineTableSource.setRight(rightSource);
            combineTableSource.setJoinType(DruidJoinTypeAdapter.getInstance().get(joinType));
            combineTableSource.setCondition(opExpr);
            this.tableSource = combineTableSource;
        } else {
            SQLJoinTableSource joinTableSource = new SQLJoinTableSource();
            SQLExprTableSource leftSource = new SQLExprTableSource(new SQLIdentifierExpr(left.getName().format()), left.getAlias());
            joinTableSource.setRight(rightSource);
            joinTableSource.setJoinType(DruidJoinTypeAdapter.getInstance().get(joinType));
            joinTableSource.setCondition(opExpr);
            joinTableSource.setLeft(leftSource);
            this.tableSource = joinTableSource;
        }
        return self();
    }

    @Override
    public SQLQueryOperator orderBy(SQLName sqlName, OrderCondition orderCondition) {
        OrderCondition selfOrderCondition = orderCondition;
        if (selfOrderCondition == null) {
            selfOrderCondition = OrderCondition.DESC;
        }
        SQLOrderingSpecification druidOrder;
        switch (selfOrderCondition) {
            case ASC:
                druidOrder = SQLOrderingSpecification.ASC;
                break;
            case DESC:
            default:
                druidOrder = SQLOrderingSpecification.DESC;
        }
        getOrderBy().addItem(new SQLIdentifierExpr(sqlName.format()), druidOrder);
        return self();
    }

    @Override
    public SQLQueryOperator limit(Long limit, Long offset) {
        getLimit().setOffset(Math.toIntExact(offset));
        getLimit().setRowCount(Math.toIntExact(limit));
        return self();
    }

    @Override
    public SQLQueryOperator groupByOnes(Collection<SQLName> fieldNames) {
        fieldNames.stream()
                .map(sqlName -> new SQLIdentifierExpr(sqlName.format()))
                .forEach(getGroupBy()::addItem);
        return self();
    }

    @Override
    public SQLQueryOperator gt(SQLName sqlName, Object value) {
        selectQuery.addWhere(
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()), SQLBinaryOperator.GreaterThan, new SQLVariantRefExpr(StringPool.QUESTION_MARK), druidDbType));
        addPrepareValue(sqlName.getName(), value);
        return self();
    }

    @Override
    public SQLQueryOperator gte(SQLName sqlName, Object value) {
        selectQuery.addWhere(
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()), SQLBinaryOperator.GreaterThanOrEqual, new SQLVariantRefExpr(StringPool.QUESTION_MARK), druidDbType));
        addPrepareValue(sqlName.getName(), value);
        return self();
    }

    @Override
    public SQLQueryOperator lt(SQLName sqlName, Object value) {
        selectQuery.addWhere(
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()), SQLBinaryOperator.LessThan, new SQLVariantRefExpr(StringPool.QUESTION_MARK), druidDbType));
        addPrepareValue(sqlName.getName(), value);
        return self();
    }

    @Override
    public SQLQueryOperator lte(SQLName sqlName, Object value) {
        selectQuery.addWhere(
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()), SQLBinaryOperator.LessThanOrEqual, new SQLVariantRefExpr(StringPool.QUESTION_MARK), druidDbType));
        addPrepareValue(sqlName.getName(), value);
        return self();
    }

    @Override
    public SQLQueryOperator eq(SQLName sqlName, Object value) {
        selectQuery.addWhere(
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()), SQLBinaryOperator.Equality, new SQLVariantRefExpr(StringPool.QUESTION_MARK), druidDbType));
        addPrepareValue(sqlName.getName(), value);
        return self();
    }

    @Override
    public SQLQueryOperator notNull(SQLName sqlName) {
        selectQuery.addWhere(
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()), SQLBinaryOperator.IsNot, new SQLNullExpr(), druidDbType));
        return self();
    }

    @Override
    public SQLQueryOperator isNull(SQLName sqlName) {
        selectQuery.addWhere(
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()), SQLBinaryOperator.Is, new SQLNullExpr(), druidDbType));
        return self();
    }

    @Override
    public SQLQueryOperator in(SQLName sqlName, Object... values) {
        SQLInListExpr sqlInListExpr = new SQLInListExpr(new SQLIdentifierExpr(sqlName.format()));
        Arrays.stream(values).forEach(value -> {
            sqlInListExpr.addTarget(new SQLVariantRefExpr(StringPool.QUESTION_MARK));
            addPrepareValue(sqlName.getName(), value);
        });
        selectQuery.addWhere(sqlInListExpr);
        return self();
    }

    @Override
    public SQLQueryOperator between(SQLName sqlName, Object withValue, Object endValue) {
        SQLBetweenExpr sqlBetweenExpr =
                new SQLBetweenExpr(new SQLIdentifierExpr(sqlName.format()), new SQLVariantRefExpr(StringPool.QUESTION_MARK), new SQLVariantRefExpr(StringPool.QUESTION_MARK));
        addPrepareValue(sqlName.getName(), withValue);
        addPrepareValue(sqlName.getName(), endValue);
        selectQuery.addWhere(sqlBetweenExpr);
        return self();
    }

    @Override
    public SQLQueryOperator notBetween(SQLName sqlName, Object withValue, Object endValue) {
        SQLBetweenExpr sqlBetweenExpr =
                new SQLBetweenExpr(new SQLIdentifierExpr(sqlName.format()), new SQLVariantRefExpr(StringPool.QUESTION_MARK), new SQLVariantRefExpr(StringPool.QUESTION_MARK));
        sqlBetweenExpr.setNot(true);
        addPrepareValue(sqlName.getName(), withValue);
        addPrepareValue(sqlName.getName(), endValue);
        selectQuery.addWhere(sqlBetweenExpr);
        return self();
    }

    @Override
    public SQLQueryOperator like(SQLName sqlName, Object value) {
        selectQuery.addWhere(
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()), SQLBinaryOperator.Like, new SQLVariantRefExpr(StringPool.QUESTION_MARK), druidDbType));
        addPrepareValue(sqlName.getName(), value);
        return self();
    }

    @Override
    public SQLQueryOperator $like(SQLName sqlName, Object value) {
        selectQuery.addWhere(
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()), SQLBinaryOperator.Like, new SQLVariantRefExpr(StringPool.QUESTION_MARK), druidDbType));
        addPrepareValue(sqlName.getName(), SQLBinaryOperator.Modulus + Types.toString(value));
        return self();
    }

    @Override
    public SQLQueryOperator like$(SQLName sqlName, Object value) {
        selectQuery.addWhere(
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()), SQLBinaryOperator.Like, new SQLVariantRefExpr(StringPool.QUESTION_MARK), druidDbType));
        addPrepareValue(sqlName.getName(), Types.toString(value) + SQLBinaryOperator.Modulus);
        return self();
    }

    @Override
    public SQLQueryOperator $like$(SQLName sqlName, Object value) {
        selectQuery.addWhere(
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()), SQLBinaryOperator.Like, new SQLVariantRefExpr(StringPool.QUESTION_MARK), druidDbType));
        addPrepareValue(sqlName.getName(), SQLBinaryOperator.Modulus + Types.toString(value) + SQLBinaryOperator.Modulus);
        return self();
    }

    @Override
    public SQLQueryOperator or() {
        throw new UnsupportedOperationException("DruidQueryOperator not support");
    }

    @Override
    public SQLQueryOperator and() {
        throw new UnsupportedOperationException("DruidQueryOperator not support");
    }

    /**
     * 获取 order by子句
     *
     * @return orderBy
     */
    private SQLOrderBy getOrderBy() {
        if (orderBy == null) {
            orderBy = new SQLOrderBy();
        }
        return orderBy;
    }

    /**
     * 获取 group by 实例
     *
     * @return groupBy
     */
    private SQLSelectGroupByClause getGroupBy() {
        if (groupBy == null) {
            groupBy = new SQLSelectGroupByClause();
        }
        return groupBy;
    }

    /**
     * 获取limit子句
     *
     * @return limit
     */
    private SQLLimit getLimit() {
        if (sqlLimit == null) {
            sqlLimit = new SQLLimit();
        }
        return sqlLimit;
    }

    @Override
    public String getPrepareSQL() {
        if (orderBy != null) {
            selectQuery.setOrderBy(orderBy);
        }
        if (groupBy != null) {
            selectQuery.setGroupBy(groupBy);
        }
        if (sqlLimit != null) {
            selectQuery.setLimit(sqlLimit);
        }
        selectQuery.setFrom(tableSource);
        return SQLUtils.toSQLString(selectQuery);
    }
}
