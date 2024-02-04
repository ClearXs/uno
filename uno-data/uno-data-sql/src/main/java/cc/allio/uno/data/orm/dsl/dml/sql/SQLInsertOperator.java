package cc.allio.uno.data.orm.dsl.dml.sql;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.bean.ValueWrapper;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.type.DBType;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.visitor.ParameterizedOutputVisitorUtils;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.data.orm.dsl.dml.InsertOperator;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Druid INSERT
 *
 * @author jiangwei
 * @date 2023/4/13 16:25
 * @since 1.1.4
 */
@AutoService(InsertOperator.class)
@Operator.Group(OperatorKey.SQL_LITERAL)
public class SQLInsertOperator extends PrepareOperatorImpl<InsertOperator> implements InsertOperator {
    private SQLInsertStatement insertStatement;
    private final DbType druidDbType;
    // 暂存于已经添加过的column
    private final ArrayList<String> columns;
    private Table table;

    public SQLInsertOperator() {
        this(DBType.getSystemDbType());
    }

    public SQLInsertOperator(DBType dbType) {
        super();
        this.druidDbType = SQLSupport.translateDb(dbType);
        this.insertStatement = new SQLInsertStatement();
        insertStatement.setDbType(druidDbType);
        this.columns = new ArrayList<>();
    }

    @Override
    public String getDSL() {
        return ParameterizedOutputVisitorUtils.restore(
                getPrepareDSL(),
                druidDbType,
                getPrepareValues().stream().map(PrepareValue::getValue).collect(Collectors.toList()));
    }

    @Override
    public InsertOperator parse(String dsl) {
        this.insertStatement = (SQLInsertStatement) SQLUtils.parseSingleStatement(dsl, druidDbType);
        List<SQLExpr> columns = insertStatement.getColumns();
        List<SQLInsertStatement.ValuesClause> valuesList = insertStatement.getValuesList();
        for (SQLInsertStatement.ValuesClause valuesClause : valuesList) {
            List<SQLExpr> values = valuesClause.getValues();
            for (int i = 0; i < values.size(); i++) {
                String column;
                try {
                    SQLExpr exprColumn = columns.get(i);
                    column = SQLSupport.getExprColumn(exprColumn);
                } catch (Throwable ex) {
                    throw new DSLException(
                            String.format("Append values not equals columns. now columns is %s, values is %s", columns, valuesClause.getValues()));
                }
                this.columns.add(column);
                SQLExpr valueExpr = values.get(i);
                Object value = SQLSupport.getExprValue(valueExpr);
                addPrepareValue(column, value);
            }
        }
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
    public InsertOperator from(Table table) {
        SQLExprTableSource tableSource = new UnoSQLExprTableSource(druidDbType);
        tableSource.setExpr(new SQLIdentifierExpr(table.getName().format()));
        tableSource.setCatalog(table.getCatalog());
        tableSource.setSchema(table.getSchema());
        insertStatement.setTableSource(tableSource);
        this.table = table;
        return self();
    }

    @Override
    public Table getTables() {
        return table;
    }


    @Override
    public InsertOperator strictFill(String f, Supplier<Object> v) {
        return strictFill(f, v, true);
    }

    @Override
    public InsertOperator columns(List<DSLName> columns) {
        List<SQLInsertStatement.ValuesClause> valuesList = insertStatement.getValuesList();
        if (valuesList.isEmpty()) {
            for (DSLName column : columns) {
                addColumn(column.format());
            }
        } else {
            for (DSLName diffColumn : columns) {
                strictFill(diffColumn.format(), () -> null, false);
            }
        }
        return self();
    }

    @Override
    public InsertOperator values(List<Object> values) {
        SQLInsertStatement.ValuesClause valuesClause = new SQLInsertStatement.ValuesClause();
        for (int i = 0; i < columns.size(); i++) {
            String column = columns.get(i);
            Object value;
            try {
                value = values.get(i);
            } catch (IndexOutOfBoundsException ex) {
                // values缺失部分 null进行填充
                value = null;
            }
            if (ValueWrapper.EMPTY_VALUE.equals(value)) {
                value = null;
            }
            valuesClause.addValue(new SQLVariantRefExpr(StringPool.QUESTION_MARK));
            addPrepareValue(column, value);
        }
        insertStatement.addValueCause(valuesClause);
        return self();
    }

    @Override
    public boolean isBatched() {
        return insertStatement.getValuesList().size() > 1;
    }

    /**
     * 数据填充API，按照column是否存在与否，批量填充或者批量覆盖数据，可以根据参数existAssign来确认存在是否覆盖数据
     *
     * @param f           column
     * @param supplier    值提供者
     * @param existAssign existAssign
     * @return SQLInsertOperator
     */
    private InsertOperator strictFill(String f, Supplier<Object> supplier, boolean existAssign) {
        // 1.在batch里面
        // 2.如果相同column值覆盖
        int index = columns.indexOf(f);
        List<SQLInsertStatement.ValuesClause> valuesList = insertStatement.getValuesList();
        Object v = supplier == null ? null : supplier.get();
        if (index < 0) {
            addColumn(f);
            index = columns.size() - 1;
            for (int i = 0; i < valuesList.size(); i++) {
                SQLInsertStatement.ValuesClause valuesClause = valuesList.get(i);
                valuesClause.addValue(new SQLVariantRefExpr(StringPool.QUESTION_MARK));
                // 相同字段在值数组中对应位置计算，即字段index + VALUES从句的索引号 * 字段长度
                int prepareIndex = index + i * columns.size();
                backward(prepareIndex, 1);
                setPrepareValue(prepareIndex, f, v);
                incrementPrepareIndex();
            }
        } else if (existAssign) {
            for (int i = 0; i < insertStatement.getValuesList().size(); i++) {
                int prepareIndex = index + i * columns.size();
                setPrepareValue(prepareIndex, f, v);
            }
        }
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
    public String getPrepareDSL() {
        return SQLUtils.toSQLString(insertStatement);
    }
}
