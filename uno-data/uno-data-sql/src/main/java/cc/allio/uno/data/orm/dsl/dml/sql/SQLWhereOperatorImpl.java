package cc.allio.uno.data.orm.dsl.dml.sql;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.type.Types;
import cc.allio.uno.core.util.Values;
import cc.allio.uno.data.orm.dsl.*;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.expr.*;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * where操作集合归类
 *
 * @author jiangwei
 * @date 2024/1/5 20:28
 * @since 1.1.7
 */
public abstract class SQLWhereOperatorImpl<T extends WhereOperator<T> & PrepareOperator<T>>
        extends PrepareOperatorImpl<T>
        implements WhereOperator<T> {

    private SQLExpr where = null;
    private LogicMode mode = LogicMode.AND;

    protected SQLWhereOperatorImpl() {
        super();
    }

    @Override
    public T gt(DSLName sqlName, Object value) {
        SQLBinaryOpExpr expr =
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()),
                        SQLBinaryOperator.GreaterThan,
                        new SQLVariantRefExpr(StringPool.QUESTION_MARK), getDruidType());
        appendAndSetWhere(expr);
        addPrepareValue(sqlName.getName(), value);
        return self();
    }

    @Override
    public T gte(DSLName sqlName, Object value) {
        SQLBinaryOpExpr expr =
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()), SQLBinaryOperator.GreaterThanOrEqual,
                        new SQLVariantRefExpr(StringPool.QUESTION_MARK),
                        getDruidType());
        appendAndSetWhere(expr);
        addPrepareValue(sqlName.getName(), value);
        return self();
    }

    @Override
    public T lt(DSLName sqlName, Object value) {
        SQLBinaryOpExpr expr =
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()),
                        SQLBinaryOperator.LessThan,
                        new SQLVariantRefExpr(StringPool.QUESTION_MARK),
                        getDruidType());
        appendAndSetWhere(expr);
        addPrepareValue(sqlName.getName(), value);
        return self();
    }

    @Override
    public T lte(DSLName sqlName, Object value) {
        SQLBinaryOpExpr expr =
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()), SQLBinaryOperator.LessThanOrEqual,
                        new SQLVariantRefExpr(StringPool.QUESTION_MARK),
                        getDruidType());
        appendAndSetWhere(expr);
        addPrepareValue(sqlName.getName(), value);
        return self();
    }

    @Override
    public T eq(DSLName sqlName, Object value) {
        SQLBinaryOpExpr expr =
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()),
                        SQLBinaryOperator.Equality,
                        new SQLVariantRefExpr(StringPool.QUESTION_MARK),
                        getDruidType());
        appendAndSetWhere(expr);
        addPrepareValue(sqlName.getName(), value);
        return self();
    }

    @Override
    public T neq(DSLName sqlName, Object value) {
        SQLBinaryOpExpr expr =
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()),
                        SQLBinaryOperator.NotEqual,
                        new SQLVariantRefExpr(StringPool.QUESTION_MARK),
                        getDruidType());
        appendAndSetWhere(expr);
        addPrepareValue(sqlName.getName(), value);
        return self();
    }

    @Override
    public T notNull(DSLName sqlName) {
        SQLBinaryOpExpr expr =
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()), SQLBinaryOperator.IsNot,
                        new SQLNullExpr(),
                        getDruidType());
        appendAndSetWhere(expr);
        return self();
    }

    @Override
    public T isNull(DSLName sqlName) {
        SQLBinaryOpExpr expr =
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()),
                        SQLBinaryOperator.Is,
                        new SQLNullExpr(),
                        getDruidType());
        appendAndSetWhere(expr);
        return self();
    }

    @Override
    public <V> T in(DSLName sqlName, V... values) {
        SQLInListExpr sqlInListExpr = new SQLInListExpr(new SQLIdentifierExpr(sqlName.format()));
        Collection<V> vs = Values.collectionExpand(values);
        for (V v : vs) {
            sqlInListExpr.addTarget(new SQLVariantRefExpr(StringPool.QUESTION_MARK));
            addPrepareValue(sqlName.getName(), v);
        }
        appendAndSetWhere(sqlInListExpr);
        return self();
    }

    @Override
    public <V> T notIn(DSLName sqlName, V... values) {
        SQLInListExpr sqlInListExpr = new SQLInListExpr(new SQLIdentifierExpr(sqlName.format()));
        sqlInListExpr.setNot(true);
        Collection<V> vs = Values.collectionExpand(values);
        for (V v : vs) {
            sqlInListExpr.addTarget(new SQLVariantRefExpr(StringPool.QUESTION_MARK));
            addPrepareValue(sqlName.getName(), v);
        }
        appendAndSetWhere(sqlInListExpr);
        return self();
    }

    @Override
    public T between(DSLName sqlName, Object withValue, Object endValue) {
        SQLBetweenExpr sqlBetweenExpr =
                new SQLBetweenExpr(
                        new SQLIdentifierExpr(sqlName.format()),
                        new SQLVariantRefExpr(StringPool.QUESTION_MARK),
                        new SQLVariantRefExpr(StringPool.QUESTION_MARK));
        addPrepareValue(sqlName.getName(), withValue);
        addPrepareValue(sqlName.getName(), endValue);
        appendAndSetWhere(sqlBetweenExpr);
        return self();
    }

    @Override
    public T notBetween(DSLName sqlName, Object withValue, Object endValue) {
        SQLBetweenExpr sqlBetweenExpr =
                new SQLBetweenExpr(
                        new SQLIdentifierExpr(sqlName.format()),
                        new SQLVariantRefExpr(StringPool.QUESTION_MARK),
                        new SQLVariantRefExpr(StringPool.QUESTION_MARK));
        sqlBetweenExpr.setNot(true);
        addPrepareValue(sqlName.getName(), withValue);
        addPrepareValue(sqlName.getName(), endValue);
        appendAndSetWhere(sqlBetweenExpr);
        return self();
    }

    @Override
    public T like(DSLName sqlName, Object value) {
        SQLBinaryOpExpr expr =
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()),
                        SQLBinaryOperator.Like,
                        new SQLVariantRefExpr(StringPool.QUESTION_MARK),
                        getDruidType());
        appendAndSetWhere(expr);
        addPrepareValue(sqlName.getName(), value);
        return self();
    }

    @Override
    public T $like(DSLName sqlName, Object value) {
        SQLBinaryOpExpr expr =
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()),
                        SQLBinaryOperator.Like,
                        new SQLVariantRefExpr(StringPool.QUESTION_MARK),
                        getDruidType());
        appendAndSetWhere(expr);
        addPrepareValue(sqlName.getName(), SQLBinaryOperator.Modulus.name + Types.toString(value));
        return self();
    }

    @Override
    public T like$(DSLName sqlName, Object value) {
        SQLBinaryOpExpr expr =
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()),
                        SQLBinaryOperator.Like,
                        new SQLVariantRefExpr(StringPool.QUESTION_MARK),
                        getDruidType());
        appendAndSetWhere(expr);
        addPrepareValue(sqlName.getName(), Types.toString(value) + SQLBinaryOperator.Modulus.name);
        return self();
    }

    @Override
    public T $like$(DSLName sqlName, Object value) {
        SQLBinaryOpExpr expr =
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()),
                        SQLBinaryOperator.Like,
                        new SQLVariantRefExpr(StringPool.QUESTION_MARK),
                        getDruidType());
        appendAndSetWhere(expr);
        addPrepareValue(sqlName.getName(), SQLBinaryOperator.Modulus.name + Types.toString(value) + SQLBinaryOperator.Modulus.name);
        return self();
    }

    @Override
    public T notLike(DSLName sqlName, Object value) {
        SQLBinaryOpExpr expr =
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()),
                        SQLBinaryOperator.NotLike,
                        new SQLVariantRefExpr(StringPool.QUESTION_MARK),
                        getDruidType());
        appendAndSetWhere(expr);
        addPrepareValue(sqlName.getName(), value);
        return self();
    }

    @Override
    public T $notLike(DSLName sqlName, Object value) {
        SQLBinaryOpExpr expr =
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()),
                        SQLBinaryOperator.NotLike,
                        new SQLVariantRefExpr(StringPool.QUESTION_MARK),
                        getDruidType());
        appendAndSetWhere(expr);
        addPrepareValue(sqlName.getName(), SQLBinaryOperator.Modulus.name + Types.toString(value));
        return self();
    }

    @Override
    public T notLike$(DSLName sqlName, Object value) {
        SQLBinaryOpExpr expr =
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()),
                        SQLBinaryOperator.NotLike,
                        new SQLVariantRefExpr(StringPool.QUESTION_MARK),
                        getDruidType());
        appendAndSetWhere(expr);
        addPrepareValue(sqlName.getName(), Types.toString(value) + SQLBinaryOperator.Modulus.name);
        return self();
    }

    @Override
    public T $notLike$(DSLName sqlName, Object value) {
        SQLBinaryOpExpr expr =
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()),
                        SQLBinaryOperator.NotLike,
                        new SQLVariantRefExpr(StringPool.QUESTION_MARK),
                        getDruidType());
        appendAndSetWhere(expr);
        addPrepareValue(sqlName.getName(), SQLBinaryOperator.Modulus.name + Types.toString(value) + SQLBinaryOperator.Modulus.name);
        return self();
    }

    @Override
    public T or() {
        switchMode(LogicMode.OR);
        return self();
    }

    @Override
    public T and() {
        switchMode(LogicMode.AND);
        return self();
    }

    /**
     * 拼接where and setValue
     *
     * @param expr expr
     */
    protected void appendAndSetWhere(SQLExpr expr) {
        this.where = mode.appendWhere(getSQLObject(), this.where, expr, getDruidType());
        Consumer<SQLExpr> setWhere = getSetWhere();
        setWhere.accept(where);
    }

    protected void switchMode(LogicMode mode) {
        this.mode = mode;
    }

    /**
     * 获取{@link DbType}实例
     *
     * @return 非null
     */
    protected abstract DbType getDruidType();

    /**
     * 获取SQL DSL实例
     *
     * @return 非null
     */
    protected abstract SQLObject getSQLObject();

    /**
     * 设置where
     *
     * @return 非null
     */
    protected abstract Consumer<SQLExpr> getSetWhere();

    @Override
    public void reset() {
        super.reset();
        this.where = null;
        this.mode = LogicMode.AND;
    }
}
