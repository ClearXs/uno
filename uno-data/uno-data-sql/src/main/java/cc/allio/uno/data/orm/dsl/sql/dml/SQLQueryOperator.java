package cc.allio.uno.data.orm.dsl.sql.dml;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.util.ClassUtils;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.core.util.template.ExpressionTemplate;
import cc.allio.uno.data.orm.dsl.Func;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.exception.DSLException;
import cc.allio.uno.data.orm.dsl.sql.SQLSupport;
import cc.allio.uno.data.orm.dsl.sql.UnoSQLExprTableSource;
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
import com.alibaba.druid.sql.visitor.SQLASTOutputVisitor;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * Druid Query Operator
 *
 * @author j.x
 * @date 2023/4/12 23:08
 * @since 1.1.4
 */
@AutoService(QueryOperator.class)
@Operator.Group(OperatorKey.SQL_LITERAL)
public class SQLQueryOperator extends SQLWhereOperatorImpl<SQLQueryOperator> implements QueryOperator<SQLQueryOperator> {

    private DBType dbType;
    private DbType druidDbType;
    private final DruidTokenOperatorAdapter tokenOperatorAdapter;

    private SQLSelectQueryBlock selectQuery;
    private SQLTableSource tableSource;
    private Table table;

    private SQLWithSubqueryClause subqueryClause;

    // order by
    private SQLOrderBy orderBy;
    // group by
    private SQLSelectGroupByClause groupBy;
    // limit by
    private SQLLimit sqlLimit;

    // select columns缓存
    private final List<String> columns = Lists.newArrayList();

    /**
     * tree query template, like as
     * <p>
     * WITH RECURSIVE biz_tree AS (SELECT xxx FROM dual WHERE xxx UNION (SELECT sub.* FROM ((SELECT xxx FROM dual WHERE xxx) sub INNER JOIN biz_tree P ON P.ID = sub.parent_id))) SELECT xxx FROM biz_tree
     * </p>
     */
    static final String TREE_QUERY_TEMPLATE = "WITH RECURSIVE biz_tree AS (#{baseQuery} UNION (SELECT sub.* FROM ((#{subQuery}) sub INNER JOIN biz_tree P ON P.ID = sub.PARENT_ID))) SELECT #{columns} FROM biz_tree";

    public SQLQueryOperator() {
        this(DBType.getSystemDbType());
    }

    public SQLQueryOperator(DBType dbType) {
        super();
        this.dbType = dbType;
        this.druidDbType = SQLSupport.translateDb(dbType);
        this.tokenOperatorAdapter = new DruidTokenOperatorAdapter();
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

    /**
     * from DSL remain where DSL
     *
     * @return the where dsl
     */
    public String getWhereDSL() {
        List<Object> parameters = getPrepareValues().stream().map(PrepareValue::getValue).toList();
        StringBuilder out = new StringBuilder();
        SQLASTOutputVisitor visitor = SQLUtils.createOutputVisitor(out, druidDbType);
        visitor.setInputParameters(parameters);
        SQLExpr where = selectQuery.getWhere();
        where.accept(visitor);
        return out.toString();
    }

    @Override
    public SQLQueryOperator parse(String dsl) {
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement(dsl, druidDbType);
        Field selectField = FieldUtils.getField(sqlStatement.getClass(), "select", true);
        if (selectField == null) {
            throw new DSLException("not found select statement field");
        }
        ClassUtils.setAccessible(selectField);
        SQLSelect sqlSelect;
        try {
            sqlSelect = (SQLSelect) selectField.get(sqlStatement);
        } catch (Throwable ex) {
            throw new DSLException(ex);
        }
        SQLSelectQueryBlock queryBlock = sqlSelect.getQueryBlock();
        this.selectQuery = queryBlock;
        this.tableSource = queryBlock.getFrom();
        List<SQLSelectItem> selectList = queryBlock.getSelectList();
        for (SQLSelectItem selectItem : selectList) {
            SQLExpr expr = selectItem.getExpr();
            String exprColumn = SQLSupport.getExprColumn(expr);
            addColumns(exprColumn, null, false);
        }
        // parse to sub query clause
        SQLWithSubqueryClause withSubQuery = sqlSelect.getWithSubQuery();
        if (withSubQuery != null) {
            this.subqueryClause = withSubQuery;
        }
        return self();
    }

    @Override
    public SQLQueryOperator customize(UnaryOperator<SQLQueryOperator> operatorFunc) {
        return operatorFunc.apply(new SQLQueryOperator(dbType));
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
        this.subqueryClause = null;
        this.columns.clear();
    }

    @Override
    public void setDBType(DBType dbType) {
        this.dbType = dbType;
        this.druidDbType = SQLSupport.translateDb(dbType);
        this.selectQuery.setDbType(this.druidDbType);
    }

    @Override
    public DBType getDBType() {
        return dbType;
    }

    @Override
    public SQLQueryOperator select(DSLName dslName) {
        addColumns(dslName.format(), null, true);
        return self();
    }

    @Override
    public SQLQueryOperator select(DSLName dslName, String alias) {
        addColumns(dslName.format(), alias, true);
        return self();
    }

    @Override
    public SQLQueryOperator selects(Collection<DSLName> dslNames) {
        for (DSLName sqlName : dslNames) {
            addColumns(sqlName.format(), null, true);
        }
        return self();
    }

    @Override
    public List<String> obtainSelectColumns() {
        return columns;
    }

    @Override
    public SQLQueryOperator distinct() {
        SQLSelectItem sqlSelectItem = new SQLSelectItem(new SQLAggregateExpr("", SQLAggregateOption.DISTINCT));
        selectQuery.addSelectItem(sqlSelectItem);
        return self();
    }

    @Override
    public SQLQueryOperator distinctOn(DSLName dslName, String alias) {
        SQLSelectItem sqlSelectItem = new SQLSelectItem(new SQLAggregateExpr(dslName.format(), SQLAggregateOption.DISTINCT));
        selectQuery.addSelectItem(sqlSelectItem);
        return self();
    }

    @Override
    public SQLQueryOperator aggregate(Func syntax, DSLName dslName, String alias, Distinct distinct) {
        SQLSelectItem sqlSelectItem = new SQLSelectItem();
        SQLAggregateExpr sqlAggregateExpr = new SQLAggregateExpr(syntax.getName());
        if (distinct != null) {
            sqlAggregateExpr.setOption(SQLAggregateOption.DISTINCT);
        }
        sqlAggregateExpr.addArgument(new SQLIdentifierExpr(dslName.format()));
        sqlSelectItem.setExpr(sqlAggregateExpr);
        selectQuery.addSelectItem(sqlSelectItem);
        return self();
    }

    @Override
    public SQLQueryOperator from(Table table) {
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
    public SQLQueryOperator from(QueryOperator<?> fromTable, String alias) {
        String fromSQL = fromTable.getDSL();
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement(fromSQL, druidDbType, true);
        SQLSubqueryTableSource from = new SQLSubqueryTableSource((SQLSelect) sqlStatement);
        from.setAlias(alias);
        this.tableSource = from;
        return self();
    }

    @Override
    public SQLQueryOperator join(Table left, JoinType joinType, Table right, BinaryCondition condition) {
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
    public SQLQueryOperator orderBy(DSLName dslName, OrderCondition orderCondition) {
        SQLOrderingSpecification druidOrder;
        if (orderCondition == OrderCondition.ASC) {
            druidOrder = SQLOrderingSpecification.ASC;
        } else {
            druidOrder = SQLOrderingSpecification.DESC;
        }
        getOrderBy().addItem(new SQLIdentifierExpr(dslName.format()), druidOrder);
        return self();
    }

    @Override
    public SQLQueryOperator limit(Long limit, Long offset) {
        getLimit().setRowCount(Math.toIntExact(limit));
        getLimit().setOffset(Math.toIntExact(offset));
        return self();
    }

    @Override
    public SQLQueryOperator groupByOnes(Collection<DSLName> fieldNames) {
        fieldNames.stream()
                .map(sqlName -> new SQLIdentifierExpr(sqlName.format()))
                .forEach(getGroupBy()::addItem);
        return self();
    }

    @Override
    public SQLQueryOperator tree(QueryOperator<?> baseQuery, QueryOperator<?> subQuery) {
        String baseQueryDsl = baseQuery.getDSL();
        String subQueryDsl = subQuery.getDSL();
        String treeQuery =
                ExpressionTemplate.parse(
                        TREE_QUERY_TEMPLATE,
                        "baseQuery", baseQueryDsl,
                        "subQuery", subQueryDsl,
                        "columns", String.join(StringPool.COMMA, baseQuery.obtainSelectColumns()));
        return customize(treeQuery);
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
        if (subqueryClause != null) {
            SQLSelect sqlSelect = new SQLSelect();
            sqlSelect.setQuery(selectQuery);
            sqlSelect.setWithSubQuery(subqueryClause);
            return SQLUtils.toSQLString(sqlSelect, druidDbType);
        } else {

            return SQLUtils.toSQLString(selectQuery, druidDbType);
        }
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
