package cc.allio.uno.data.orm.sql;

/**
 * SQL操作
 *
 * @author jiangwei
 * @date 2023/4/12 19:44
 * @since 1.1.4
 */
public interface SQLOperator<T extends SQLOperator<T>> {

    /**
     * 获取SQL字符串
     *
     * @return SQL字符串
     */
    String getSQL();

    /**
     * 解析SQL
     *
     * @param sql sql
     * @return SQLOperator
     */
    T parse(String sql);

    /**
     * 重制当前SQL已经存在的数据
     */
    void reset();
}
