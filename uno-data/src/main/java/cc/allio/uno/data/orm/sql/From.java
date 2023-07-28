package cc.allio.uno.data.orm.sql;

/**
 * SQL FROM语句
 *
 * @author jiangwei
 * @date 2023/1/6 18:02
 * @since 1.1.4
 * @deprecated 1.1.4版本删除
 */
@Deprecated
public interface From<T extends From<T>> extends Statement<T> {

    /**
     * 给定被注解{@link javax.persistence.Table}标识的实体，从其中获取表名称
     *
     * @param tableEntity 实体
     * @return FROM
     */
    T from(Class<?> tableEntity) throws SQLException;

    /**
     * FROM table
     *
     * @param table table名称
     * @return FROM
     */
    T from(String table) throws SQLException;

    /**
     * FROM table
     *
     * @param table table名称
     * @param alias alias别名
     * @return FROM
     */
    T from(String table, String alias);

}
