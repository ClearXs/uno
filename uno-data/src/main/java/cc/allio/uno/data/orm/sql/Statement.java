package cc.allio.uno.data.orm.sql;

import cc.allio.uno.data.orm.sql.dml.local.expression.Expression;

import java.util.Collection;
import java.util.Comparator;

/**
 * SQL标识接口，用于SQl相关语句的构建声明
 *
 * @author jiangwei
 * @date 2022/9/30 10:22
 * @since 1.1.0
 * @deprecated 1.1.4版本删除
 */
@Deprecated
public interface Statement<T extends Statement<T>> {

    // --------------- SQL常量 ---------------

    String IN = "IN";
    String IS_NULL = "IS NUll";
    String NOT_NULL = "NOT NULL";
    String LT = "lt";
    String LT_SYMBOL = "<";
    String LTE = "lte";
    String LTE_SYMBOL = "<=";
    String GT = "gt";
    String GT_SYMBOL = ">";
    String GTE = "gte";
    String GTE_SYMBOL = ">=";
    String EQ = "eq";
    String EQ_SYMBOL = "=";
    String ASC = "ASC";
    String DESC = "DESC";
    String AND = "AND";
    String OR = "OR";
    String BETWEEN = "BETWEEN";
    String NOT_BETWEEN = "NOT BETWEEN";
    String AS = "AS";
    String LIKE = "LIKE";
    String $LIKE = "%";
    String SELECT = "SELECT";
    String FROM = "FROM";
    String WHERE = "WHERE";
    String ORDER = "ORDER";
    String GROUP_BY = "GROUP BY";
    String LIMIT = "LIMIT";
    String OFFSET = "OFFSET";

    /**
     * 返回自身实例
     *
     * @return self
     */
    default T self() {
        return (T) this;
    }

    /**
     * 声明实现类需要返回sql字符串
     *
     * @return SQL字符串
     * @throws SQLException 获取并解析SQL发生异常时抛出
     */
    String getSQL() throws SQLException;

    /**
     * 获取条件语句如：
     * <ul>
     *     <li>Select a , b -> a, b </li>
     *     <li>where a=1 -> a=1</li>
     * </ul>
     *
     * @return condition
     */
     String getCondition();

    /**
     * 获取当前语句表达式实例列表。
     *
     * @return Expression列表
     * @see Expression
     */
    Collection<Expression> getExpressions();

    /**
     * 做语句语法校验
     *
     * @throws SQLException 当语句没有通过时抛出该异常
     */
    void syntaxCheck() throws SQLException;

    // 语句排序
    int SELECT_ORDER = 0;
    int FROM_ORDER = 1;
    int WHERE_ORDER = 2;
    int GROUP_ORDER = 3;
    int ORDER_ORDER = 4;
    int LIMIT_ORDER = 5;

    /**
     * 获取语句执行顺序
     *
     * @return 顺序
     * @see OrderComparator
     */
    int order();

    /**
     * 默认排序比较器
     */
    OrderComparator ORDER_COMPARATOR = new OrderComparator();

    /**
     * 语句排序比较器
     */
    class OrderComparator implements Comparator<Statement<?>> {

        @Override
        public int compare(Statement<?> o1, Statement<?> o2) {
            return Integer.compare(o1.order(), o2.order());
        }
    }
}
