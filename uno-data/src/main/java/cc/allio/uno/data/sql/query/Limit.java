package cc.allio.uno.data.sql.query;

import cc.allio.uno.data.sql.Statement;

/**
 * limit
 *
 * @author jiangwei
 * @date 2023/1/11 14:52
 * @since 1.1.4
 */
public interface Limit<T extends Limit<T>> extends Statement<T> {

    /**
     * LIMIT { number | ALL }
     *
     * @param number 起始行数
     * @return Limit
     */
    T limit(Integer number);

    /**
     * [ OFFSET number ]
     *
     * @param number 偏移位置
     * @return Limit
     */
    T offset(Integer number);

    /**
     * 分页
     *
     * @param current  当前页
     * @param pageSize 页大小
     * @return Limit
     */
    default T page(int current, int pageSize) {
        return limit((current - 1) * pageSize).offset(pageSize);
    }
}
