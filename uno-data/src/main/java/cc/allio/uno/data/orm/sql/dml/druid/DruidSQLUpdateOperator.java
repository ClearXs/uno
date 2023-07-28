package cc.allio.uno.data.orm.sql.dml.druid;

import cc.allio.uno.data.orm.type.DBType;
import cc.allio.uno.data.orm.type.druid.DruidDbTypeAdapter;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLUpdateSetItem;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.alibaba.druid.sql.visitor.ParameterizedOutputVisitorUtils;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.type.Types;
import cc.allio.uno.data.orm.sql.PrepareValue;
import cc.allio.uno.data.orm.sql.SQLName;
import cc.allio.uno.data.orm.sql.SQLPrepareOperatorImpl;
import cc.allio.uno.data.orm.sql.Table;
import cc.allio.uno.data.orm.sql.dml.SQLUpdateOperator;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * DruidSQLUpdateOperator
 *
 * @author jiangwei
 * @date 2023/4/16 18:18
 * @since 1.1.4
 */
public class DruidSQLUpdateOperator extends SQLPrepareOperatorImpl<SQLUpdateOperator> implements SQLUpdateOperator {

    private final DbType druidDbType;

    private SQLUpdateStatement updateStatement;

    /**
     * 记录update where开始的索引
     */
    private int wherePrepareIndex;

    /**
     * where条件值的大小
     */
    private int whereSize;

    public DruidSQLUpdateOperator(DBType dbType) {
        super();
        this.druidDbType = DruidDbTypeAdapter.getInstance().get(dbType);
        this.updateStatement = new SQLUpdateStatement();
        updateStatement.setDbType(druidDbType);
    }

    @Override
    public String getSQL() {
        return ParameterizedOutputVisitorUtils.restore(
                getPrepareSQL(),
                druidDbType,
                getPrepareValues().stream().map(PrepareValue::getValue).collect(Collectors.toList()));
    }

    @Override
    public SQLUpdateOperator parse(String sql) {
        throw new UnsupportedOperationException("DruidSQLUpdateOperator not support");
    }

    @Override
    public void reset() {
        super.reset();
        this.updateStatement = new SQLUpdateStatement();
        updateStatement.setDbType(druidDbType);
    }

    @Override
    public SQLUpdateOperator from(Table table) {
        SQLExprTableSource tableSource = new SQLExprTableSource();
        tableSource.setExpr(new SQLIdentifierExpr(table.getName().format()));
        tableSource.setCatalog(table.getCatalog());
        updateStatement.setFrom(tableSource);
        updateStatement.setTableSource(tableSource);
        return self();
    }

    @Override
    public SQLUpdateOperator gt(SQLName sqlName, Object value) {
        updateStatement.addWhere(
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()), SQLBinaryOperator.GreaterThan, new SQLVariantRefExpr(StringPool.QUESTION_MARK), druidDbType));
        addPrepareValue(sqlName.getName(), value);
        return self();
    }

    @Override
    public SQLUpdateOperator gte(SQLName sqlName, Object value) {
        updateStatement.addWhere(
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()), SQLBinaryOperator.GreaterThanOrEqual, new SQLVariantRefExpr(StringPool.QUESTION_MARK), druidDbType));
        addPrepareValue(sqlName.getName(), value);
        return self();
    }

    @Override
    public SQLUpdateOperator lt(SQLName sqlName, Object value) {
        updateStatement.addWhere(
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()), SQLBinaryOperator.LessThan, new SQLVariantRefExpr(StringPool.QUESTION_MARK), druidDbType));
        addPrepareValue(sqlName.getName(), value);
        return self();
    }

    @Override
    public SQLUpdateOperator lte(SQLName sqlName, Object value) {
        updateStatement.addWhere(
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()), SQLBinaryOperator.LessThanOrEqual, new SQLVariantRefExpr(StringPool.QUESTION_MARK), druidDbType));
        addPrepareValue(sqlName.getName(), value);
        return self();
    }

    @Override
    public SQLUpdateOperator eq(SQLName sqlName, Object value) {
        updateStatement.addWhere(
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()), SQLBinaryOperator.Equality, new SQLVariantRefExpr(StringPool.QUESTION_MARK), druidDbType));
        addPrepareValue(sqlName.getName(), value);
        return self();
    }

    @Override
    public SQLUpdateOperator notNull(SQLName sqlName) {
        updateStatement.addWhere(
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()), SQLBinaryOperator.IsNot, new SQLNullExpr(), druidDbType));
        return self();
    }

    @Override
    public SQLUpdateOperator isNull(SQLName sqlName) {
        updateStatement.addWhere(
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()), SQLBinaryOperator.Is, new SQLNullExpr(), druidDbType));
        return self();
    }

    @Override
    public SQLUpdateOperator in(SQLName sqlName, Object... values) {
        SQLInListExpr sqlInListExpr = new SQLInListExpr(new SQLIdentifierExpr(sqlName.format()));
        Arrays.stream(values).forEach(value -> {
            sqlInListExpr.addTarget(new SQLVariantRefExpr(StringPool.QUESTION_MARK));
            addPrepareValue(sqlName.getName(), value);
        });
        updateStatement.addWhere(sqlInListExpr);
        return self();
    }

    @Override
    public SQLUpdateOperator between(SQLName sqlName, Object withValue, Object endValue) {
        SQLBetweenExpr sqlBetweenExpr =
                new SQLBetweenExpr(new SQLIdentifierExpr(sqlName.format()), new SQLVariantRefExpr(StringPool.QUESTION_MARK), new SQLVariantRefExpr(StringPool.QUESTION_MARK));
        addPrepareValue(sqlName.getName(), withValue);
        addPrepareValue(sqlName.getName(), endValue);
        updateStatement.addWhere(sqlBetweenExpr);
        return self();
    }

    @Override
    public SQLUpdateOperator notBetween(SQLName sqlName, Object withValue, Object endValue) {
        SQLBetweenExpr sqlBetweenExpr =
                new SQLBetweenExpr(new SQLIdentifierExpr(sqlName.format()), new SQLVariantRefExpr(StringPool.QUESTION_MARK), new SQLVariantRefExpr(StringPool.QUESTION_MARK));
        sqlBetweenExpr.setNot(true);
        addPrepareValue(sqlName.getName(), withValue);
        addPrepareValue(sqlName.getName(), endValue);
        updateStatement.addWhere(sqlBetweenExpr);
        return self();
    }

    @Override
    public SQLUpdateOperator like(SQLName sqlName, Object value) {
        updateStatement.addWhere(
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()), SQLBinaryOperator.Like, new SQLVariantRefExpr(StringPool.QUESTION_MARK), druidDbType));
        addPrepareValue(sqlName.getName(), value);
        return self();
    }

    @Override
    public SQLUpdateOperator $like(SQLName sqlName, Object value) {
        updateStatement.addWhere(
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()), SQLBinaryOperator.Like, new SQLVariantRefExpr(StringPool.QUESTION_MARK), druidDbType));
        addPrepareValue(sqlName.getName(), SQLBinaryOperator.Modulus + Types.toString(value));
        return self();
    }

    @Override
    public SQLUpdateOperator like$(SQLName sqlName, Object value) {
        updateStatement.addWhere(
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()), SQLBinaryOperator.Like, new SQLVariantRefExpr(StringPool.QUESTION_MARK), druidDbType));
        addPrepareValue(sqlName.getName(), Types.toString(value) + SQLBinaryOperator.Modulus);
        return self();
    }

    @Override
    public SQLUpdateOperator $like$(SQLName sqlName, Object value) {
        updateStatement.addWhere(
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()), SQLBinaryOperator.Like, new SQLVariantRefExpr(StringPool.QUESTION_MARK), druidDbType));
        addPrepareValue(sqlName.getName(), SQLBinaryOperator.Modulus + Types.toString(value) + SQLBinaryOperator.Modulus);
        return self();
    }

    @Override
    public SQLUpdateOperator or() {
        throw new UnsupportedOperationException("DruidSQLUpdateOperator not support");
    }

    @Override
    public SQLUpdateOperator and() {
        throw new UnsupportedOperationException("DruidSQLUpdateOperator not support");
    }

    @Override
    public SQLUpdateOperator updates(Map<SQLName, Object> values) {
        for (Map.Entry<SQLName, Object> columnValue : values.entrySet()) {
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
    public String getPrepareSQL() {
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
     * 如果遇到非where条件，则把where数据进行挪动，让非where数据记录到where最后一个
     */
    private void addPrepareValue(String column, Object value, boolean isWhere) {
        if (isWhere) {
            super.addPrepareValue(column, value);
            whereSize++;
        } else {
            ensureCapacityInternal(prepareValues.length + 1);
            PrepareValue[] updateReplace = ArrayUtils.subarray(prepareValues, wherePrepareIndex, wherePrepareIndex + whereSize);
            prepareValues[wherePrepareIndex] = PrepareValue.of(prepareIndex++, column, value);
            // update数据进行替换
            for (int i = 0; i < updateReplace.length; i++) {
                prepareValues[wherePrepareIndex + i + 1] = updateReplace[i];
            }
            wherePrepareIndex++;
        }
    }
}
