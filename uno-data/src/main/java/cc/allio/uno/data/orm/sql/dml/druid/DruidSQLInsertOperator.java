package cc.allio.uno.data.orm.sql.dml.druid;

import cc.allio.uno.data.orm.sql.PrepareValue;
import cc.allio.uno.data.orm.sql.SQLName;
import cc.allio.uno.data.orm.sql.SQLPrepareOperatorImpl;
import cc.allio.uno.data.orm.sql.Table;
import cc.allio.uno.data.orm.type.DBType;
import cc.allio.uno.data.orm.type.druid.DruidDbTypeAdapter;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.visitor.ParameterizedOutputVisitorUtils;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.data.orm.sql.dml.SQLInsertOperator;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Druid INSERT
 *
 * @author jiangwei
 * @date 2023/4/13 16:25
 * @since 1.1.4
 */
public class DruidSQLInsertOperator extends SQLPrepareOperatorImpl<SQLInsertOperator> implements SQLInsertOperator {

    private SQLInsertStatement insertStatement;
    private final DbType druidDbType;
    // 暂存于已经添加过的column
    private final Set<String> columns;

    public DruidSQLInsertOperator(DBType dbType) {
        super();
        this.druidDbType = DruidDbTypeAdapter.getInstance().get(dbType);
        this.insertStatement = new SQLInsertStatement();
        insertStatement.setDbType(druidDbType);
        this.columns = Sets.newHashSet();
    }

    @Override
    public String getSQL() {
        return ParameterizedOutputVisitorUtils.restore(
                getPrepareSQL(),
                druidDbType,
                getPrepareValues().stream().map(PrepareValue::getValue).collect(Collectors.toList()));
    }

    @Override
    public SQLInsertOperator parse(String sql) {
        List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, druidDbType);
        insertStatement = (SQLInsertStatement) sqlStatements.get(0);
        insertStatement.setDbType(druidDbType);
        return self();
    }

    @Override
    public void reset() {
        super.reset();
        this.insertStatement = new SQLInsertStatement();
        insertStatement.setDbType(druidDbType);
        columns.clear();
    }

    @Override
    public SQLInsertOperator from(Table table) {
        SQLExprTableSource tableSource = new SQLExprTableSource();
        tableSource.setExpr(new SQLIdentifierExpr(table.getName().format()));
        tableSource.setCatalog(table.getCatalog());
        insertStatement.setTableSource(tableSource);
        return self();
    }

    @Override
    public SQLInsertOperator inserts(Map<SQLName, Object> values) {
        SQLInsertStatement.ValuesClause valuesClause = new SQLInsertStatement.ValuesClause();
        for (Map.Entry<SQLName, Object> columnValue : values.entrySet()) {
            String column = columnValue.getKey().format();
            Object value = columnValue.getValue();
            addColumn(column);
            valuesClause.addValue(new SQLVariantRefExpr(StringPool.QUESTION_MARK));
            addPrepareValue(columnValue.getKey().getName(), value);
        }
        // 验证value是否与columns一致
        if (valuesClause.getValues().size() != columns.size()) {
            throw new IllegalArgumentException(
                    String.format("Append values not equals columns. now columns is %s, values is %s", columns, valuesClause.getValues()));
        }
        insertStatement.addValueCause(valuesClause);
        return self();
    }

    @Override
    public SQLInsertOperator batchInserts(Collection<SQLName> columns, Collection<Collection<Object>> values) {
        // 添加column
        for (SQLName sqlName : columns) {
            addColumn(sqlName.getName());
        }
        List<SQLName> newColumns = Lists.newArrayList(columns);
        // 添加value
        AtomicInteger index = new AtomicInteger(0);
        values.stream().flatMap(value -> {
                    // match and verifying
                    SQLInsertStatement.ValuesClause valuesClause = new SQLInsertStatement.ValuesClause();
                    value.forEach(nowValue -> {
                        SQLName matchColumn;
                        try {
                            matchColumn = newColumns.get(index.getAndIncrement());
                        } catch (ArrayIndexOutOfBoundsException ex) {
                            throw new IllegalArgumentException("Append values more than columns.", ex);
                        }
                        valuesClause.addValue(new SQLVariantRefExpr(StringPool.QUESTION_MARK));
                        addPrepareValue(matchColumn.getName(), nowValue);
                    });
                    index.set(0);
                    return Stream.of(valuesClause);
                })
                .forEach(valuesClause -> {
                    // 验证
                    if (valuesClause.getValues().size() != columns.size()) {
                        throw new IllegalArgumentException(
                                String.format("Append values not equals columns. now columns is %s, values is %s", columns, valuesClause.getValues()));
                    }
                    insertStatement.addValueCause(valuesClause);
                });
        return self();
    }

    /**
     * 添加column到缓存与{@link SQLInsertStatement}，如果已经存在则不进行添加。
     *
     * @param column column
     */
    private void addColumn(String column) {
        if (!columns.contains(column)) {
            columns.add(column);
            insertStatement.addColumn(new SQLIdentifierExpr(column));
        }
    }

    @Override
    public String getPrepareSQL() {
        return SQLUtils.toSQLString(insertStatement);
    }

}
