package cc.allio.uno.data.orm.dsl;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.function.ConsumerAction;
import cc.allio.uno.core.function.SupplierAction;
import cc.allio.uno.core.function.TernaryConsumer;
import cc.allio.uno.core.function.VoidConsumer;
import cc.allio.uno.core.type.Types;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.dsl.type.DSLType;
import cc.allio.uno.data.orm.dsl.type.DataType;
import cc.allio.uno.data.orm.dsl.type.DruidDbTypeAdapter;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.expr.*;
import com.google.common.collect.Lists;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

/**
 * druid sql 相关工具类
 *
 * @author jiangwei
 * @date 2024/1/4 17:06
 * @since 1.1.6
 */
public final class SQLSupport implements Self<SQLSupport> {

    private static final List<DbType> CURRENT_SUPPORT_DB = List.of(DbType.mysql, DbType.postgresql, DbType.db2, DbType.h2);
    private static final Object EMPTY = new Object();

    private static final List<SQLBinaryOperator> BINARY_VALUABLE_OPERATOR = Lists.newArrayList();

    private static final SQLVariantRefExpr PLACEHOLDER = new SQLVariantRefExpr(StringPool.QUESTION_MARK);

    static {
        BINARY_VALUABLE_OPERATOR.add(SQLBinaryOperator.Like);
        BINARY_VALUABLE_OPERATOR.add(SQLBinaryOperator.NotLike);
        BINARY_VALUABLE_OPERATOR.add(SQLBinaryOperator.Equality);
        BINARY_VALUABLE_OPERATOR.add(SQLBinaryOperator.GreaterThan);
        BINARY_VALUABLE_OPERATOR.add(SQLBinaryOperator.LessThan);
        BINARY_VALUABLE_OPERATOR.add(SQLBinaryOperator.GreaterThanOrEqual);
        BINARY_VALUABLE_OPERATOR.add(SQLBinaryOperator.LessThanOrEqual);
    }

    private final Operator<?> druidOperator;
    private Throwable ex;
    private final List<VoidConsumer> internalActions;
    private Object obj;

    private VoidConsumer exAction;

    private SQLSupport(Operator<?> druidOperator) {
        this.druidOperator = druidOperator;
        this.internalActions = Lists.newArrayList();
    }

    /**
     * 基于{@link Operator}实体创建帮助类
     *
     * @param druidOperator druidOperator
     * @return DruidSQLSupport
     */
    public static SQLSupport on(Operator<?> druidOperator) {
        return new SQLSupport(druidOperator);
    }

    /**
     * 目前支持的DB
     */
    public static boolean isSupportDb(DbType dbType) {
        return CURRENT_SUPPORT_DB.contains(dbType);
    }

    /**
     * 是否支持db
     *
     * @param dbType dbtype
     * @return DruidSQLSupport
     */
    public SQLSupport onDb(DbType dbType) {
        this.internalActions.add(() -> {
            boolean supportDb = isSupportDb(dbType);
            if (!supportDb) {
                this.ex = new DSLException(String.format("Operator %s nonsupport db type for %s", this.druidOperator.getClass().getName(), dbType));
            }
        });
        return self();
    }

    public SQLSupport then(SupplierAction<?> supplier) {
        this.internalActions.add(() -> this.obj = supplier.get());
        return self();
    }

    public SQLSupport then(VoidConsumer action) {
        this.internalActions.add(() -> action.accept(EMPTY));
        return self();
    }

    /**
     * 当support 发生异常时回掉获取异常信息
     *
     * @param action action
     * @return DruidSQLSupport
     */
    public SQLSupport onException(ConsumerAction<Throwable> action) {
        this.exAction = () -> action.accept(ex);
        return self();
    }

    /**
     * 翻译{@link DBType}为druid的{@link DbType}类型
     *
     * @param systemDb systemDb
     * @return druid db
     */
    public static DbType translateDb(DBType systemDb) {
        return DruidDbTypeAdapter.getInstance().adapt(systemDb);
    }

    /**
     * 反译druid{@link DbType}为{@link DBType}
     *
     * @param druidDb druidDb
     * @return DBType
     */
    public static DBType reversalDb(DbType druidDb) {
        return DruidDbTypeAdapter.getInstance().reverse(druidDb);
    }

    /**
     * 未支持，调用该action将会抛出异常
     *
     * @param operateName operateName
     * @return DruidSQLSupport
     */
    public SQLSupport onNonsupport(String operateName) {
        this.internalActions.add(() -> this.ex = nonsupportOperate(this.druidOperator, operateName));
        return self();
    }

    /**
     * 未支持操作
     *
     * @throws DSLException 构建信息并抛出该异常
     */
    public static DSLException nonsupportOperate(Operator<?> druidOperator, String operateName) {
        String message = String.format("Operator %s nonsupport %s operate", druidOperator.getClass().getName(), operateName);
        return new DSLException(message);
    }

    /**
     * 启动
     *
     * @return 由使用者提供
     * @throws DSLException 当action执行过程中发生异常则进行包装成{@link DSLException}返回
     */
    public <T> T execute() {
        for (VoidConsumer action : internalActions) {
            if (ex != null) {
                exAction.accept(ex);
                return (T) new DSLException(ex);
            }
            try {
                action.accept(EMPTY);
            } catch (Throwable ex) {
                this.ex = ex;
            }
        }
        if (ex != null) {
            return (T) new DSLException(ex);
        }
        return (T) obj;
    }

    /**
     * 判断{@link SQLExpr}是否是{@link com.alibaba.druid.sql.ast.SQLName}，如果是则返回，否则返回null
     *
     * @param expr expr
     * @return
     */
    public static String getExprColumn(SQLExpr expr) {
        if (isNameExpr(expr)) {
            return ((SQLName) expr).getSimpleName();
        }
        return expr.toString();
    }

    /**
     * 判断{@link SQLExpr}是否是{@link SQLValuableExpr}，如果是则返回，否则返回null
     *
     * @param expr expr
     * @return
     */
    public static Object getExprValue(SQLExpr expr) {
        if (isValuableExpr(expr)) {
            return ((SQLValuableExpr) expr).getValue();
        }
        return null;
    }

    /**
     * 判断{@link SQLExpr}是否是{@link SQLBinaryOpExpr}
     *
     * @param expr
     * @return
     */
    public static boolean isBinaryExpr(SQLExpr expr) {
        return SQLBinaryOpExpr.class.isAssignableFrom(expr.getClass());
    }

    /**
     * 判断给定的expr是否是{@link com.alibaba.druid.sql.ast.expr.SQLBetweenExpr}
     *
     * @param expr expr
     * @return
     */
    public static boolean isBetweenExpr(SQLExpr expr) {
        return SQLBetweenExpr.class.isAssignableFrom(expr.getClass());
    }

    /**
     * 判断给定的expr是否是{@link com.alibaba.druid.sql.ast.expr.SQLInListExpr}
     *
     * @param expr expr
     * @return
     */
    public static boolean isInExpr(SQLExpr expr) {
        return SQLInListExpr.class.isAssignableFrom(expr.getClass());
    }

    /**
     * 判断给定的expr是否是{@link SQLExpr}
     *
     * @param expr expr
     * @return
     */
    public static boolean isNullExpr(SQLExpr expr) {
        return SQLNullExpr.class.isAssignableFrom(expr.getClass());
    }

    /**
     * 判断给定expr是否是{@link SQLName}
     *
     * @param expr expr
     * @return
     */
    public static boolean isNameExpr(SQLExpr expr) {
        return SQLName.class.isAssignableFrom(expr.getClass());
    }

    /**
     * 判断给定expr是否是{@link SQLValuableExpr}
     *
     * @param expr expr
     * @return
     */
    public static boolean isValuableExpr(SQLExpr expr) {
        return SQLValuableExpr.class.isAssignableFrom(expr.getClass());
    }

    /**
     * 判断给定的{@link SQLBinaryOperator}是否是值操作
     *
     * @param binaryOperator binaryOperator
     * @return
     */
    public static boolean isBinaryValuableOperator(SQLBinaryOperator binaryOperator) {
        return BINARY_VALUABLE_OPERATOR.contains(binaryOperator);
    }

    /**
     * 遍历{@link SQLBinaryOpExpr}，存在如下情况进行回调:
     * <ul>
     *     <li>判断是否是值类型{@link #isBinaryValuableOperator(SQLBinaryOperator)}，如果是则它的left结点是column expr，right结点是value expr</li>
     *     <li>如果是{@link #isBetweenExpr(SQLExpr)}类型，则调用{@link #betweenTrigger(SQLBetweenExpr, LogicMode, TernaryConsumer)}转换并触发</li>
     *     <li>如果是{@link #isInExpr(SQLExpr)}类型，则调用{@link #inTrigger(SQLInListExpr, LogicMode, TernaryConsumer)}转换并触发</li>
     *     <li>如果是{@link #isNullExpr(SQLExpr)}类型，可以得到它是is null等语句</li>
     * </ul>
     *
     * @param binaryOpExpr binaryOpExpr
     * @param trigger      当满足上述条件时，进行回调，接受两个类型，一个是包装后的expr实例（增加{@link #PLACEHOLDER}的占位符），另外一个是预处理的参数值类型
     */
    public static void binaryExprTraversal(SQLBinaryOpExpr binaryOpExpr, TernaryConsumer<SQLExpr, LogicMode, List<Tuple2<String, Object>>> trigger) {
        SQLExpr left = binaryOpExpr.getLeft();
        SQLExpr right = binaryOpExpr.getRight();
        if (isBinaryExpr(left)) {
            binaryExprTraversal((SQLBinaryOpExpr) left, trigger);
        }
        if (isBinaryExpr(right)) {
            binaryExprTraversal((SQLBinaryOpExpr) right, trigger);
        }
        SQLBinaryOperator operator = binaryOpExpr.getOperator();
        LogicMode mode = toMode(operator);
        if (isBinaryValuableOperator(operator)) {
            // 值类型
            String exprColumn = getExprColumn(left);
            Object exprValue = getExprValue(right);
            trigger.accept(
                    new SQLBinaryOpExpr(left, operator, PLACEHOLDER),
                    mode,
                    List.of(Tuples.of(exprColumn, exprValue)));
        } else {
            if (isBetweenExpr(left)) {
                betweenTrigger((SQLBetweenExpr) left, mode, trigger);
            }
            if (isBetweenExpr(right)) {
                betweenTrigger((SQLBetweenExpr) right, mode, trigger);
            }
            if (isInExpr(left)) {
                inTrigger((SQLInListExpr) left, mode, trigger);
            }
            if (isInExpr(right)) {
                inTrigger((SQLInListExpr) right, mode, trigger);
            }
            if (isNullExpr(right)) {
                trigger.accept(new SQLBinaryOpExpr(left, operator, right), mode, Collections.emptyList());
            }
        }
    }

    /**
     * between转换触发
     *
     * @param betweenExpr betweenExpr
     * @param trigger     trigger
     */
    private static void betweenTrigger(SQLBetweenExpr betweenExpr, LogicMode mode, TernaryConsumer<SQLExpr, LogicMode, List<Tuple2<String, Object>>> trigger) {
        SQLExpr testExpr = betweenExpr.getTestExpr();
        String exprColumn = getExprColumn(testExpr);
        SQLExpr beginExpr = betweenExpr.getBeginExpr();
        Object beginValue = getExprValue(beginExpr);
        Tuple2<String, Object> begin = Tuples.of(exprColumn, beginValue);
        SQLExpr endExpr = betweenExpr.getEndExpr();
        Object exprValue = getExprValue(endExpr);
        Tuple2<String, Object> end = Tuples.of(exprColumn, exprValue);
        SQLBetweenExpr newBetweenExpr = new SQLBetweenExpr(testExpr, PLACEHOLDER, PLACEHOLDER);
        newBetweenExpr.setNot(betweenExpr.isNot());
        trigger.accept(
                newBetweenExpr,
                mode,
                List.of(begin, end));
    }

    /**
     * in转换触发
     *
     * @param inExpr  inExpr
     * @param trigger trigger
     */
    private static void inTrigger(SQLInListExpr inExpr, LogicMode mode, TernaryConsumer<SQLExpr, LogicMode, List<Tuple2<String, Object>>> trigger) {
        SQLExpr expr = inExpr.getExpr();
        String exprColumn = getExprColumn(expr);
        List<SQLExpr> targetList = inExpr.getTargetList();
        SQLInListExpr newInExpr = new SQLInListExpr();
        newInExpr.setExpr(expr);
        newInExpr.setNot(inExpr.isNot());
        List<Tuple2<String, Object>> consumeResource =
                targetList.stream()
                        .map(target -> {
                            newInExpr.addTarget(PLACEHOLDER);
                            Object exprValue = getExprValue(target);
                            return Tuples.of(exprColumn, exprValue);
                        })
                        .toList();
        trigger.accept(newInExpr, mode, consumeResource);
    }

    private static LogicMode toMode(SQLBinaryOperator operator) {
        if (operator == SQLBinaryOperator.BooleanAnd) {
            return LogicMode.AND;
        } else if (operator == SQLBinaryOperator.BooleanOr) {
            return LogicMode.OR;
        }
        return LogicMode.AND;
    }

    /**
     * 基于数据类型和值，生成{@code SQLValuableExpr}实例。遍历Java已有数据类型
     *
     * @param dataType dataType
     * @param value    value
     * @return SQLValuableExpr
     */
    public static SQLValuableExpr newSQLValue(DataType dataType, Object value) {
        DSLType sqlType = dataType.getSqlType();
        if (DSLType.CHAR.equals(sqlType)
                || DSLType.VARCHAR.equals(sqlType)
                || DSLType.LONGNVARCHAR.equals(sqlType)
                || DSLType.LONGVARCHAR.equals(sqlType)
                || DSLType.LONGVARBINARY.equals(sqlType)
                || DSLType.VARBINARY.equals(sqlType)
                || DSLType.NVARCHAR.equals(sqlType)) {
            return new SQLCharExpr(Types.parseString(value));
        }
        if (DSLType.DATE.equals(sqlType)) {
            return new SQLDateExpr(Types.parseDate(value));
        }
        if (DSLType.TIME.equals(sqlType)) {
            return new SQLTimeExpr(Types.parseDate(value), TimeZone.getDefault());
        }
        if (DSLType.TIMESTAMP.equals(sqlType)) {
            return new SQLTimestampExpr(Types.parseDate(value));
        }
        if (DSLType.DECIMAL.equals(sqlType) || DSLType.NUMBER.equals(sqlType)) {
            return new SQLDecimalExpr(Types.parseBigDecimal(value));
        }
        if (DSLType.INTEGER.equals(sqlType)) {
            return new SQLIntegerExpr(Types.getInteger(value));
        }
        if (DSLType.TINYINT.equals(sqlType)) {
            return new SQLTinyIntExpr(Types.parseByte(value));
        }
        if (DSLType.SMALLINT.equals(sqlType)) {
            return new SQLSmallIntExpr(Types.parseShort(value));
        }
        if (DSLType.BIGINT.equals(sqlType)) {
            return new SQLBigIntExpr(Types.parseLong(value));
        }
        if (DSLType.FLOAT.equals(sqlType)) {
            return new SQLFloatExpr(Types.parseFloat(value));
        }
        if (DSLType.DOUBLE.equals(sqlType)) {
            return new SQLDoubleExpr(Types.parseDouble(value));
        }
        return new SQLNullExpr();
    }
}
