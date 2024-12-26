package cc.allio.uno.data.orm.dsl.sql.dml;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.exception.DSLException;
import cc.allio.uno.data.orm.dsl.sql.SQLSupport;
import cc.allio.uno.data.orm.dsl.sql.UnoSQLExprTableSource;
import cc.allio.uno.data.orm.dsl.type.DBType;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLUpdateSetItem;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.alibaba.druid.sql.visitor.ParameterizedOutputVisitorUtils;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.data.orm.dsl.dml.UpdateOperator;
import org.apache.commons.lang3.ArrayUtils;
import reactor.util.function.Tuple2;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * DruidSQLUpdateOperator
 *
 * @author j.x
 * @since 1.1.4
 */
@AutoService(UpdateOperator.class)
@Operator.Group(OperatorKey.SQL_LITERAL)
public class SQLUpdateOperator extends SQLWhereOperatorImpl<SQLUpdateOperator> implements UpdateOperator<SQLUpdateOperator> {

    private DBType dbType;
    private DbType druidDbType;
    private Table table;
    private SQLUpdateStatement updateStatement;

    /**
     * 记录update where开始的索引
     */
    private int wherePrepareIndex;

    /**
     * where条件值的大小
     */
    private int whereSize;

    public SQLUpdateOperator() {
        this(DBType.getSystemDbType());
    }

    public SQLUpdateOperator(DBType dbType) {
        super();
        this.dbType = dbType;
        this.druidDbType = SQLSupport.translateDb(dbType);
        this.updateStatement = new SQLUpdateStatement();
        updateStatement.setDbType(druidDbType);
    }

    @Override
    public String getDSL() {
        return ParameterizedOutputVisitorUtils.restore(
                getPrepareDSL(),
                druidDbType,
                getPrepareValues().stream().map(PrepareValue::getValue).toList());
    }

    @Override
    public SQLUpdateOperator parse(String dsl) {
        this.updateStatement = (SQLUpdateStatement) SQLUtils.parseSingleStatement(dsl, druidDbType);
        // 重构建update使之成为prepare dsl
        List<SQLUpdateSetItem> items = this.updateStatement.getItems();
        if (CollectionUtils.isNotEmpty(items)) {
            List<SQLUpdateSetItem> newItems = items.stream()
                    .map(item -> {
                        String columnName = SQLSupport.getExprColumn(item.getColumn());
                        Object value = SQLSupport.getExprValue(item.getValue());
                        addPrepareValue(columnName, value);
                        SQLUpdateSetItem updateSetItem = new SQLUpdateSetItem();
                        updateSetItem.setColumn(item.getColumn());
                        updateSetItem.setValue(new SQLVariantRefExpr(StringPool.QUESTION_MARK));
                        return updateSetItem;
                    })
                    .toList();
            items.clear();
            items.addAll(newItems);
        }
        SQLExpr where = this.updateStatement.getWhere();
        if (SQLSupport.isBinaryExpr(where)) {
            this.updateStatement.setWhere(null);
            SQLSupport.binaryExprTraversal(
                    (SQLBinaryOpExpr) where,
                    (newExpr, mode, prepareValues) -> {
                        switchMode(mode);
                        appendAndSetWhere(newExpr);
                        for (Tuple2<String, Object> prepareValue : prepareValues) {
                            addPrepareValue(prepareValue.getT1(), prepareValue.getT2());
                        }
                    });
        }
        return self();
    }

    @Override
    public SQLUpdateOperator customize(UnaryOperator<SQLUpdateOperator> operatorFunc) {
        return operatorFunc.apply(new SQLUpdateOperator(dbType));
    }

    @Override
    public void reset() {
        super.reset();
        this.updateStatement = new SQLUpdateStatement();
        updateStatement.setDbType(druidDbType);
        this.whereSize = 0;
        this.wherePrepareIndex = 0;
    }

    @Override
    public void setDBType(DBType dbType) {
        this.dbType = dbType;
        this.druidDbType = SQLSupport.translateDb(dbType);
        this.updateStatement.setDbType(this.druidDbType);
    }

    @Override
    public DBType getDBType() {
        return dbType;
    }

    @Override
    public SQLUpdateOperator from(Table table) {
        SQLExprTableSource tableSource = new UnoSQLExprTableSource(druidDbType);
        tableSource.setExpr(new SQLIdentifierExpr(table.getName().format()));
        tableSource.setCatalog(table.getCatalog());
        tableSource.setSchema(table.getSchema());
        this.table = table;
        updateStatement.setFrom(tableSource);
        updateStatement.setTableSource(tableSource);
        return self();
    }

    @Override
    public Table getTable() {
        return table;
    }

    @Override
    protected DbType getDruidType() {
        return druidDbType;
    }

    @Override
    protected SQLObject getSQLObject() {
        return updateStatement;
    }

    @Override
    protected Consumer<SQLExpr> getSetWhere() {
        return where -> updateStatement.setWhere(where);
    }

    @Override
    public SQLUpdateOperator updates(Map<DSLName, Object> values) {
        if (CollectionUtils.isEmpty(values)) {
            throw new DSLException("update needs not empty values");
        }
        for (Map.Entry<DSLName, Object> columnValue : values.entrySet()) {
            String column = columnValue.getKey().format();
            Object value = columnValue.getValue();
            SQLUpdateSetItem updateSetItem = new SQLUpdateSetItem();
            updateSetItem.setColumn(new SQLIdentifierExpr(column));
            updateSetItem.setValue(new SQLVariantRefExpr(StringPool.QUESTION_MARK));
            updateStatement.addItem(updateSetItem);
            addPrepareValue(columnValue.getKey().getName(), value, false);
        }
        return self();
    }

    @Override
    public SQLUpdateOperator strictFill(String f, Supplier<Object> v) {
        // set语句
        List<String> columns =
                updateStatement.getItems()
                        .stream()
                        .map(SQLUpdateSetItem::getColumn)
                        .map(SQLSupport::getExprColumn)
                        .toList();
        Object value = v == null ? null : v.get();
        int index = columns.indexOf(f);
        if (index < 0) {
            update(f, value);
        } else {
            setPrepareValue(index, f, value);
        }
        return self();
    }

    @Override
    public String getPrepareDSL() {
        return SQLUtils.toSQLString(updateStatement);
    }

    /**
     * 在where查询中，这部分是没有问题。如果在update，导致update xx where column = ?的占位符的值排序到前面，导致数据插入问题
     */
    @Override
    protected void addPrepareValue(String column, Object value) {
        addPrepareValue(column, value, true);
    }

    /**
     * 如果遇到非where条件，则把where数据进行挪动，让非where数据记录到where之前
     */
    private void addPrepareValue(String column, Object value, boolean isWhere) {
        if (isWhere) {
            super.addPrepareValue(column, value);
            whereSize++;
        } else {
            ensureExplicitCapacity(prepareValues.length + 1);
            PrepareValue[] updateReplace = ArrayUtils.subarray(prepareValues, wherePrepareIndex, wherePrepareIndex + whereSize);
            prepareValues[wherePrepareIndex] = PrepareValue.of(prepareIndex++, column, getRealityValue(value));
            // update数据进行替换
            for (int i = 0; i < updateReplace.length; i++) {
                prepareValues[wherePrepareIndex + i + 1] = updateReplace[i];
            }
            wherePrepareIndex++;
        }
    }
}
