package cc.allio.uno.data.orm.dsl.sql.dml;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.core.type.Types;
import cc.allio.uno.core.util.Values;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.sql.LogicMode;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.expr.*;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * where操作集合归类
 *
 * @author j.x
 * @since 1.1.7
 */
public abstract class SQLWhereOperatorImpl<T extends WhereOperator<T> & PrepareOperator<T>>
        extends PrepareOperatorImpl<T> implements WhereOperator<T> {

    private SQLExpr where = null;
    private LogicMode mode = LogicMode.AND;

    protected SQLWhereOperatorImpl() {
        super();
    }

    @Override
    public T gt(DSLName sqlName, Object value) {
        // set placeholder value
        SQLVariantRefExpr placeholder = new SQLVariantRefExpr(StringPool.QUESTION_MARK);
        placeholder.setIndex(prepareIndex);
        addPrepareValue(sqlName.getName(), value);
        SQLIdentifierExpr identifierExpr = new SQLIdentifierExpr(sqlName.format());
        SQLBinaryOpExpr expr =
                new SQLBinaryOpExpr(
                        identifierExpr,
                        SQLBinaryOperator.GreaterThan,
                        placeholder,
                        getDruidType());
        appendAndSetWhere(expr);
        return self();
    }

    @Override
    public T gte(DSLName sqlName, Object value) {
        // set placeholder value
        SQLVariantRefExpr placeholder = new SQLVariantRefExpr(StringPool.QUESTION_MARK);
        placeholder.setIndex(prepareIndex);
        addPrepareValue(sqlName.getName(), value);
        SQLIdentifierExpr identifierExpr = new SQLIdentifierExpr(sqlName.format());
        SQLBinaryOpExpr expr =
                new SQLBinaryOpExpr(
                        identifierExpr,
                        SQLBinaryOperator.GreaterThanOrEqual,
                        placeholder,
                        getDruidType());
        appendAndSetWhere(expr);
        return self();
    }

    @Override
    public T lt(DSLName sqlName, Object value) {
        SQLVariantRefExpr placeholder = new SQLVariantRefExpr(StringPool.QUESTION_MARK);
        placeholder.setIndex(prepareIndex);
        addPrepareValue(sqlName.getName(), value);
        SQLIdentifierExpr identifierExpr = new SQLIdentifierExpr(sqlName.format());
        SQLBinaryOpExpr expr =
                new SQLBinaryOpExpr(
                        identifierExpr,
                        SQLBinaryOperator.LessThan,
                        new SQLVariantRefExpr(StringPool.QUESTION_MARK),
                        getDruidType());
        appendAndSetWhere(expr);
        return self();
    }

    @Override
    public T lte(DSLName sqlName, Object value) {
        SQLVariantRefExpr placeholder = new SQLVariantRefExpr(StringPool.QUESTION_MARK);
        placeholder.setIndex(prepareIndex);
        addPrepareValue(sqlName.getName(), value);
        SQLIdentifierExpr identifierExpr = new SQLIdentifierExpr(sqlName.format());
        SQLBinaryOpExpr expr =
                new SQLBinaryOpExpr(
                        identifierExpr,
                        SQLBinaryOperator.LessThanOrEqual,
                        placeholder,
                        getDruidType());
        appendAndSetWhere(expr);
        return self();
    }

    @Override
    public T eq(DSLName sqlName, Object value) {
        SQLVariantRefExpr placeholder = new SQLVariantRefExpr(StringPool.QUESTION_MARK);
        placeholder.setIndex(prepareIndex);
        addPrepareValue(sqlName.getName(), value);
        SQLIdentifierExpr identifierExpr = new SQLIdentifierExpr(sqlName.format());
        SQLBinaryOpExpr expr =
                new SQLBinaryOpExpr(
                        identifierExpr,
                        SQLBinaryOperator.Equality,
                        placeholder,
                        getDruidType());
        appendAndSetWhere(expr);
        return self();
    }

    @Override
    public T neq(DSLName sqlName, Object value) {
        SQLVariantRefExpr placeholder = new SQLVariantRefExpr(StringPool.QUESTION_MARK);
        placeholder.setIndex(prepareIndex);
        addPrepareValue(sqlName.getName(), value);
        SQLIdentifierExpr identifierExpr = new SQLIdentifierExpr(sqlName.format());
        SQLBinaryOpExpr expr =
                new SQLBinaryOpExpr(
                        identifierExpr,
                        SQLBinaryOperator.NotEqual,
                        placeholder,
                        getDruidType());
        appendAndSetWhere(expr);
        return self();
    }

    @Override
    public T notNull(DSLName sqlName) {
        SQLBinaryOpExpr expr =
                new SQLBinaryOpExpr(
                        new SQLIdentifierExpr(sqlName.format()),
                        SQLBinaryOperator.IsNot,
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
            SQLVariantRefExpr placeholder = new SQLVariantRefExpr(StringPool.QUESTION_MARK);
            placeholder.setIndex(prepareIndex);
            sqlInListExpr.addTarget(placeholder);
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
            SQLVariantRefExpr placeholder = new SQLVariantRefExpr(StringPool.QUESTION_MARK);
            placeholder.setIndex(prepareIndex);
            sqlInListExpr.addTarget(placeholder);
            addPrepareValue(sqlName.getName(), v);
        }
        appendAndSetWhere(sqlInListExpr);
        return self();
    }

    @Override
    public T between(DSLName sqlName, Object withValue, Object endValue) {
        SQLVariantRefExpr withPlaceholder = new SQLVariantRefExpr(StringPool.QUESTION_MARK);
        withPlaceholder.setIndex(prepareIndex);
        addPrepareValue(sqlName.getName(), withValue);
        SQLVariantRefExpr endPlaceholder = new SQLVariantRefExpr(StringPool.QUESTION_MARK);
        endPlaceholder.setIndex(prepareIndex);
        addPrepareValue(sqlName.getName(), endValue);
        SQLIdentifierExpr identifierExpr = new SQLIdentifierExpr(sqlName.format());
        SQLBetweenExpr sqlBetweenExpr = new SQLBetweenExpr(identifierExpr, withPlaceholder, endPlaceholder);
        appendAndSetWhere(sqlBetweenExpr);
        return self();
    }

    @Override
    public T notBetween(DSLName sqlName, Object withValue, Object endValue) {
        SQLVariantRefExpr withPlaceholder = new SQLVariantRefExpr(StringPool.QUESTION_MARK);
        withPlaceholder.setIndex(prepareIndex);
        addPrepareValue(sqlName.getName(), withValue);
        SQLVariantRefExpr endPlaceholder = new SQLVariantRefExpr(StringPool.QUESTION_MARK);
        endPlaceholder.setIndex(prepareIndex);
        addPrepareValue(sqlName.getName(), endValue);
        SQLIdentifierExpr identifierExpr = new SQLIdentifierExpr(sqlName.format());
        SQLBetweenExpr sqlBetweenExpr = new SQLBetweenExpr(identifierExpr, withPlaceholder, endPlaceholder);
        sqlBetweenExpr.setNot(true);
        appendAndSetWhere(sqlBetweenExpr);
        return self();
    }

    @Override
    public T like(DSLName sqlName, Object value) {
        SQLVariantRefExpr placeholder = new SQLVariantRefExpr(StringPool.QUESTION_MARK);
        placeholder.setIndex(prepareIndex);
        addPrepareValue(sqlName.getName(), value);
        SQLIdentifierExpr identifierExpr = new SQLIdentifierExpr(sqlName.format());
        SQLBinaryOpExpr expr =
                new SQLBinaryOpExpr(
                        identifierExpr,
                        SQLBinaryOperator.Like,
                        placeholder,
                        getDruidType());
        appendAndSetWhere(expr);
        return self();
    }

    @Override
    public T $like(DSLName sqlName, Object value) {
        SQLVariantRefExpr placeholder = new SQLVariantRefExpr(StringPool.QUESTION_MARK);
        placeholder.setIndex(prepareIndex);
        addPrepareValue(sqlName.getName(), SQLBinaryOperator.Modulus.name + Types.toString(value));
        SQLIdentifierExpr identifierExpr = new SQLIdentifierExpr(sqlName.format());
        SQLBinaryOpExpr expr =
                new SQLBinaryOpExpr(
                        identifierExpr,
                        SQLBinaryOperator.Like,
                        placeholder,
                        getDruidType());
        appendAndSetWhere(expr);
        return self();
    }

    @Override
    public T like$(DSLName sqlName, Object value) {
        SQLVariantRefExpr placeholder = new SQLVariantRefExpr(StringPool.QUESTION_MARK);
        placeholder.setIndex(prepareIndex);
        addPrepareValue(sqlName.getName(), Types.toString(value) + SQLBinaryOperator.Modulus.name);
        SQLIdentifierExpr identifierExpr = new SQLIdentifierExpr(sqlName.format());
        SQLBinaryOpExpr expr =
                new SQLBinaryOpExpr(
                        identifierExpr,
                        SQLBinaryOperator.Like,
                        placeholder,
                        getDruidType());
        appendAndSetWhere(expr);
        return self();
    }

    @Override
    public T $like$(DSLName sqlName, Object value) {
        SQLVariantRefExpr placeholder = new SQLVariantRefExpr(StringPool.QUESTION_MARK);
        placeholder.setIndex(prepareIndex);
        addPrepareValue(sqlName.getName(), SQLBinaryOperator.Modulus.name + Types.toString(value) + SQLBinaryOperator.Modulus.name);
        SQLIdentifierExpr identifierExpr = new SQLIdentifierExpr(sqlName.format());
        SQLBinaryOpExpr expr =
                new SQLBinaryOpExpr(
                        identifierExpr,
                        SQLBinaryOperator.Like,
                        placeholder,
                        getDruidType());
        appendAndSetWhere(expr);
        return self();
    }

    @Override
    public T notLike(DSLName sqlName, Object value) {
        SQLVariantRefExpr placeholder = new SQLVariantRefExpr(StringPool.QUESTION_MARK);
        placeholder.setIndex(prepareIndex);
        addPrepareValue(sqlName.getName(), value);
        SQLIdentifierExpr identifierExpr = new SQLIdentifierExpr(sqlName.format());
        SQLBinaryOpExpr expr =
                new SQLBinaryOpExpr(
                        identifierExpr,
                        SQLBinaryOperator.NotLike,
                        placeholder,
                        getDruidType());
        appendAndSetWhere(expr);
        return self();
    }

    @Override
    public T $notLike(DSLName sqlName, Object value) {
        SQLVariantRefExpr placeholder = new SQLVariantRefExpr(StringPool.QUESTION_MARK);
        placeholder.setIndex(prepareIndex);
        addPrepareValue(sqlName.getName(), SQLBinaryOperator.Modulus.name + Types.toString(value));
        SQLIdentifierExpr identifierExpr = new SQLIdentifierExpr(sqlName.format());
        SQLBinaryOpExpr expr =
                new SQLBinaryOpExpr(
                        identifierExpr,
                        SQLBinaryOperator.NotLike,
                        placeholder,
                        getDruidType());
        appendAndSetWhere(expr);
        return self();
    }

    @Override
    public T notLike$(DSLName sqlName, Object value) {
        SQLVariantRefExpr placeholder = new SQLVariantRefExpr(StringPool.QUESTION_MARK);
        placeholder.setIndex(prepareIndex);
        addPrepareValue(sqlName.getName(), Types.toString(value) + SQLBinaryOperator.Modulus.name);
        SQLIdentifierExpr identifierExpr = new SQLIdentifierExpr(sqlName.format());
        SQLBinaryOpExpr expr =
                new SQLBinaryOpExpr(
                        identifierExpr,
                        SQLBinaryOperator.NotLike,
                        placeholder,
                        getDruidType());
        appendAndSetWhere(expr);
        return self();
    }

    @Override
    public T $notLike$(DSLName sqlName, Object value) {
        SQLVariantRefExpr placeholder = new SQLVariantRefExpr(StringPool.QUESTION_MARK);
        placeholder.setIndex(prepareIndex);
        addPrepareValue(sqlName.getName(), SQLBinaryOperator.Modulus.name + Types.toString(value) + SQLBinaryOperator.Modulus.name);
        SQLIdentifierExpr identifierExpr = new SQLIdentifierExpr(sqlName.format());
        SQLBinaryOpExpr expr =
                new SQLBinaryOpExpr(
                        identifierExpr,
                        SQLBinaryOperator.NotLike,
                        placeholder,
                        getDruidType());
        appendAndSetWhere(expr);
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

    @Override
    public T nor() {
        throw Exceptions.unOperate("nor");
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
