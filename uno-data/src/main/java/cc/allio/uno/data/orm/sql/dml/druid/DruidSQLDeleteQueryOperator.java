package cc.allio.uno.data.orm.sql.dml.druid;

import cc.allio.uno.data.orm.sql.PrepareValue;
import cc.allio.uno.data.orm.sql.SQLName;
import cc.allio.uno.data.orm.sql.SQLPrepareOperatorImpl;
import cc.allio.uno.data.orm.sql.Table;
import cc.allio.uno.data.orm.type.DBType;
import cc.allio.uno.data.orm.type.druid.DruidDbTypeAdapter;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.visitor.ParameterizedOutputVisitorUtils;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.type.Types;
import cc.allio.uno.data.orm.sql.dml.SQLDeleteOperator;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * DruidSQLDeleteQueryOperator
 *
 * @author jiangwei
 * @date 2023/4/16 18:43
 * @since 1.1.4
 */
public class DruidSQLDeleteQueryOperator extends SQLPrepareOperatorImpl<SQLDeleteOperator> implements SQLDeleteOperator {

    private SQLDeleteStatement deleteStatement;
    private final DbType druidDbType;

    public DruidSQLDeleteQueryOperator(DBType dbType) {
        super();
        this.druidDbType = DruidDbTypeAdapter.getInstance().get(dbType);
        this.deleteStatement = new SQLDeleteStatement();
        deleteStatement.setDbType(druidDbType);
    }

    @Override
    public String getSQL() {
        return ParameterizedOutputVisitorUtils.restore(
                getPrepareSQL(),
                druidDbType,
                getPrepareValues().stream().map(PrepareValue::getValue).collect(Collectors.toList()));

    }

    @Override
    public SQLDeleteOperator parse(String sql) {
        return null;
    }

    @Override
    public void reset() {
        super.reset();
        this.deleteStatement = new SQLDeleteStatement();
        deleteStatement.setDbType(druidDbType);
    }

    @Override
    public String getPrepareSQL() {
        return SQLUtils.toSQLString(deleteStatement);
    }

    @Override
    public SQLDeleteOperator from(Table table) {
        SQLExprTableSource tableSource = new SQLExprTableSource();
        tableSource.setExpr(new SQLIdentifierExpr(table.getName().format()));
        tableSource.setCatalog(table.getCatalog());
        deleteStatement.setFrom(tableSource);
        return self();
    }

    @Override
    public SQLDeleteOperator gt(SQLName sqlName, Object value) {
        deleteStatement.addWhere(
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()), SQLBinaryOperator.GreaterThan, new SQLVariantRefExpr(StringPool.QUESTION_MARK), druidDbType));
        addPrepareValue(sqlName.getName(), value);
        return self();
    }

    @Override
    public SQLDeleteOperator gte(SQLName sqlName, Object value) {
        deleteStatement.addWhere(
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()), SQLBinaryOperator.GreaterThanOrEqual, new SQLVariantRefExpr(StringPool.QUESTION_MARK), druidDbType));
        addPrepareValue(sqlName.getName(), value);
        return self();
    }

    @Override
    public SQLDeleteOperator lt(SQLName sqlName, Object value) {
        deleteStatement.addWhere(
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()), SQLBinaryOperator.LessThan, new SQLVariantRefExpr(StringPool.QUESTION_MARK), druidDbType));
        addPrepareValue(sqlName.getName(), value);
        return self();
    }

    @Override
    public SQLDeleteOperator lte(SQLName sqlName, Object value) {
        deleteStatement.addWhere(
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()), SQLBinaryOperator.LessThanOrEqual, new SQLVariantRefExpr(StringPool.QUESTION_MARK), druidDbType));
        addPrepareValue(sqlName.getName(), value);
        return self();
    }

    @Override
    public SQLDeleteOperator eq(SQLName sqlName, Object value) {
        deleteStatement.addWhere(
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()), SQLBinaryOperator.Equality, new SQLVariantRefExpr(StringPool.QUESTION_MARK), druidDbType));
        addPrepareValue(sqlName.getName(), value);
        return self();
    }

    @Override
    public SQLDeleteOperator notNull(SQLName sqlName) {
        deleteStatement.addWhere(
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()), SQLBinaryOperator.IsNot, new SQLNullExpr(), druidDbType));
        return self();
    }

    @Override
    public SQLDeleteOperator isNull(SQLName sqlName) {
        deleteStatement.addWhere(
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()), SQLBinaryOperator.Is, new SQLNullExpr(), druidDbType));
        return self();
    }

    @Override
    public SQLDeleteOperator in(SQLName sqlName, Object... values) {
        SQLInListExpr sqlInListExpr = new SQLInListExpr(new SQLIdentifierExpr(sqlName.format()));
        Arrays.stream(values).forEach(value -> {
            sqlInListExpr.addTarget(new SQLVariantRefExpr(StringPool.QUESTION_MARK));
            addPrepareValue(sqlName.getName(), value);
        });
        deleteStatement.addWhere(sqlInListExpr);
        return self();
    }

    @Override
    public SQLDeleteOperator between(SQLName sqlName, Object withValue, Object endValue) {
        SQLBetweenExpr sqlBetweenExpr =
                new SQLBetweenExpr(new SQLIdentifierExpr(sqlName.format()), new SQLVariantRefExpr(StringPool.QUESTION_MARK), new SQLVariantRefExpr(StringPool.QUESTION_MARK));
        addPrepareValue(sqlName.getName(), withValue);
        addPrepareValue(sqlName.getName(), endValue);
        deleteStatement.addWhere(sqlBetweenExpr);
        return self();
    }

    @Override
    public SQLDeleteOperator notBetween(SQLName sqlName, Object withValue, Object endValue) {
        SQLBetweenExpr sqlBetweenExpr =
                new SQLBetweenExpr(new SQLIdentifierExpr(sqlName.format()), new SQLVariantRefExpr(StringPool.QUESTION_MARK), new SQLVariantRefExpr(StringPool.QUESTION_MARK));
        sqlBetweenExpr.setNot(true);
        addPrepareValue(sqlName.getName(), withValue);
        addPrepareValue(sqlName.getName(), endValue);
        deleteStatement.addWhere(sqlBetweenExpr);
        return self();
    }

    @Override
    public SQLDeleteOperator like(SQLName sqlName, Object value) {
        deleteStatement.addWhere(
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()), SQLBinaryOperator.Like, new SQLVariantRefExpr(StringPool.QUESTION_MARK), druidDbType));
        addPrepareValue(sqlName.getName(), value);
        return self();
    }

    @Override
    public SQLDeleteOperator $like(SQLName sqlName, Object value) {
        deleteStatement.addWhere(
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()), SQLBinaryOperator.Like, new SQLVariantRefExpr(StringPool.QUESTION_MARK), druidDbType));
        addPrepareValue(sqlName.getName(), SQLBinaryOperator.Modulus + Types.toString(value));
        return self();
    }

    @Override
    public SQLDeleteOperator like$(SQLName sqlName, Object value) {
        deleteStatement.addWhere(
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()), SQLBinaryOperator.Like, new SQLVariantRefExpr(StringPool.QUESTION_MARK), druidDbType));
        addPrepareValue(sqlName.getName(), Types.toString(value) + SQLBinaryOperator.Modulus);
        return self();
    }

    @Override
    public SQLDeleteOperator $like$(SQLName sqlName, Object value) {
        deleteStatement.addWhere(
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()), SQLBinaryOperator.Like, new SQLVariantRefExpr(StringPool.QUESTION_MARK), druidDbType));
        addPrepareValue(sqlName.getName(), SQLBinaryOperator.Modulus + Types.toString(value) + SQLBinaryOperator.Modulus);
        return self();
    }

    @Override
    public SQLDeleteOperator or() {
        throw new UnsupportedOperationException("DruidQueryOperator not support");
    }

    @Override
    public SQLDeleteOperator and() {
        throw new UnsupportedOperationException("DruidQueryOperator not support");
    }
}
