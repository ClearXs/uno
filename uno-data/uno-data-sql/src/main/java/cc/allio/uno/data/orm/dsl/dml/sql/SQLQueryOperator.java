package cc.allio.uno.data.orm.dsl.dml.sql;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.util.ClassUtils;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.data.orm.dsl.Func;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.word.Distinct;
import cc.allio.uno.data.orm.dsl.type.DBType;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.*;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.visitor.ParameterizedOutputVisitorUtils;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * Druid Query Operator
 *
 * @author jiangwei
 * @date 2023/4/12 23:08
 * @since 1.1.4
 */
@AutoService(QueryOperator.class)
@Operator.Group(OperatorKey.SQL_LITERAL)
public class SQLQueryOperator extends SQLWhereOperatorImpl<QueryOperator> implements QueryOperator {

    private final DbType druidDbType;
    private final DruidTokenOperatorAdapter tokenOperatorAdapter;

    private SQLSelectQueryBlock selectQuery;
    private SQLTableSource tableSource;
    private Table table;

    // order by
    private SQLOrderBy orderBy;
    // group by
    private SQLSelectGroupByClause groupBy;
    // limit by
    private SQLLimit sqlLimit;

    // select columns缓存
    private final List<String> columns = Lists.newArrayList();

    public SQLQueryOperator() {
        this(DBType.getSystemDbType());
    }

    public SQLQueryOperator(DBType dbType) {
        super();
        this.tokenOperatorAdapter = new DruidTokenOperatorAdapter();
        this.druidDbType = SQLSupport.translateDb(dbType);
        this.selectQuery = new SQLSelectQueryBlock();
        selectQuery.setDbType(druidDbType);
    }

    @Override
    public String getDSL() {
        List<Object> values = getPrepareValues().stream().map(PrepareValue::getValue).toList();
        if (CollectionUtils.isEmpty(values)) {
            return getPrepareDSL();
        }
        return ParameterizedOutputVisitorUtils.restore(
                getPrepareDSL(),
                druidDbType,
                getPrepareValues().stream().map(PrepareValue::getValue).toList());
    }

    @Override
    public QueryOperator parse(String dsl) {
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement(dsl, druidDbType);
        Field selectField = FieldUtils.getField(sqlStatement.getClass(), "select", true);
        if (selectField == null) {
            throw new DSLException("not found select statement field");
        }
        ClassUtils.setAccessible(selectField);
        try {
            SQLSelect sqlSelect = (SQLSelect) selectField.get(sqlStatement);
            SQLSelectQueryBlock queryBlock = sqlSelect.getQueryBlock();
            this.selectQuery = queryBlock;
            this.tableSource = queryBlock.getFrom();
            List<SQLSelectItem> selectList = queryBlock.getSelectList();
            for (SQLSelectItem selectItem : selectList) {
                SQLExpr expr = selectItem.getExpr();
                String exprColumn = SQLSupport.getExprColumn(expr);
                addColumns(exprColumn, null, false);
            }
        } catch (Throwable ex) {
            throw new DSLException(ex);
        }
        return self();
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
    public QueryOperator select(DSLName sqlName) {
        addColumns(sqlName.format(), null, true);
        return self();
    }

    @Override
    public QueryOperator select(DSLName sqlName, String alias) {
        addColumns(sqlName.format(), alias, true);
        return self();
    }

    @Override
    public QueryOperator selects(Collection<DSLName> sqlNames) {
        for (DSLName sqlName : sqlNames) {
            addColumns(sqlName.format(), null, true);
        }
        return self();
    }

    @Override
    public QueryOperator distinct() {
        SQLSelectItem sqlSelectItem = new SQLSelectItem(new SQLAggregateExpr("", SQLAggregateOption.DISTINCT));
        selectQuery.addSelectItem(sqlSelectItem);
        return self();
    }

    @Override
    public QueryOperator distinctOn(DSLName sqlName, String alias) {
        SQLSelectItem sqlSelectItem = new SQLSelectItem(new SQLAggregateExpr(sqlName.format(), SQLAggregateOption.DISTINCT));
        selectQuery.addSelectItem(sqlSelectItem);
        return self();
    }

    @Override
    public QueryOperator aggregate(Func syntax, DSLName sqlName, String alias, Distinct distinct) {
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
    public QueryOperator from(Table table) {
        SQLExprTableSource tableSource = new UnoSQLExprTableSource(druidDbType);
        tableSource.setExpr(new SQLIdentifierExpr(table.getName().format()));
        tableSource.setCatalog(table.getCatalog());
        tableSource.setSchema(table.getSchema());
        this.table = table;
        this.tableSource = tableSource;
        return self();
    }

    @Override
    public Table getTable() {
        return table;
    }

    @Override
    public QueryOperator from(QueryOperator fromTable, String alias) {
        String fromSQL = fromTable.getDSL();
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement(fromSQL, druidDbType, true);
        SQLSubqueryTableSource from = new SQLSubqueryTableSource((SQLSelect) sqlStatement);
        from.setAlias(alias);
        this.tableSource = from;
        return self();
    }

    @Override
    public QueryOperator join(Table left, JoinType joinType, Table right, BinaryCondition condition) {
        // 构建连接关系
        SQLExprTableSource rightSource = new UnoSQLExprTableSource(druidDbType);
        rightSource.setExpr(new SQLIdentifierExpr(right.getName().format()));
        rightSource.setCatalog(right.getCatalog());
        rightSource.setSchema(right.getSchema());
        SQLBinaryOperator operator = tokenOperatorAdapter.adapt(condition.getSyntax());
        SQLBinaryOpExpr opExpr = new SQLBinaryOpExpr(new SQLIdentifierExpr(condition.getLeft()), operator, new SQLIdentifierExpr(condition.getRight()));
        // 判断是否已经包含连接关系，如果则组建复合关系
        if (tableSource instanceof SQLJoinTableSource) {
            SQLJoinTableSource combineTableSource = new SQLJoinTableSource();
            tableSource.setAlias(left.getAlias());
            combineTableSource.setLeft(tableSource);
            combineTableSource.setRight(rightSource);
            combineTableSource.setJoinType(DruidJoinTypeAdapter.getInstance().adapt(joinType));
            combineTableSource.setCondition(opExpr);
            this.tableSource = combineTableSource;
        } else {
            SQLJoinTableSource joinTableSource = new SQLJoinTableSource();
            SQLExprTableSource leftSource = new SQLExprTableSource(new SQLIdentifierExpr(left.getName().format()), left.getAlias());
            joinTableSource.setRight(rightSource);
            joinTableSource.setJoinType(DruidJoinTypeAdapter.getInstance().adapt(joinType));
            joinTableSource.setCondition(opExpr);
            joinTableSource.setLeft(leftSource);
            this.tableSource = joinTableSource;
        }
        return self();
    }

    @Override
    public QueryOperator orderBy(DSLName sqlName, OrderCondition orderCondition) {
        SQLOrderingSpecification druidOrder;
        if (orderCondition == OrderCondition.ASC) {
            druidOrder = SQLOrderingSpecification.ASC;
        } else {
            druidOrder = SQLOrderingSpecification.DESC;
        }
        getOrderBy().addItem(new SQLIdentifierExpr(sqlName.format()), druidOrder);
        return self();
    }

    @Override
    public QueryOperator limit(Long limit, Long offset) {
        getLimit().setOffset(Math.toIntExact(offset));
        getLimit().setRowCount(Math.toIntExact(limit));
        return self();
    }

    @Override
    public QueryOperator groupByOnes(Collection<DSLName> fieldNames) {
        fieldNames.stream()
                .map(sqlName -> new SQLIdentifierExpr(sqlName.format()))
                .forEach(getGroupBy()::addItem);
        return self();
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
    public String getPrepareDSL() {
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

    @Override
    protected DbType getDruidType() {
        return druidDbType;
    }

    @Override
    protected SQLObject getSQLObject() {
        return selectQuery;
    }

    @Override
    protected Consumer<SQLExpr> getSetWhere() {
        return where -> selectQuery.setWhere(where);
    }

    /**
     * 添加column
     *
     * @param column        column
     * @param alias         别名
     * @param appendedDruid 是否追加至druid中
     */
    private void addColumns(String column, String alias, boolean appendedDruid) {
        if (columns.contains(column)) {
            return;
        }
        columns.add(column);
        if (appendedDruid) {
            SQLSelectItem sqlSelectItem;
            if (StringPool.ASTERISK.equals(column)) {
                sqlSelectItem = new SQLSelectItem(new SQLAllColumnExpr());
            } else if (StringUtils.isNotBlank(alias)) {
                sqlSelectItem = new SQLSelectItem(new SQLIdentifierExpr(column), alias);
            } else {
                sqlSelectItem = new SQLSelectItem(new SQLIdentifierExpr(column));
            }
            selectQuery.addSelectItem(sqlSelectItem);
        }
    }
}
