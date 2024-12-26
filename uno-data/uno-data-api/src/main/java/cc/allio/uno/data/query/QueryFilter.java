package cc.allio.uno.data.query;

/**
 * QueryFilter
 *
 * @author j.x
 * @since 1.1.4
 */
public interface QueryFilter {

    /**
     * 获取查询的sql
     *
     * @return sql
     */
    String getDSL();

    /**
     * setValue query wrapper
     *
     */
    void setQueryWrapper(QueryWrapper queryWrapper);

    /**
     * 获取 query wrapper
     *
     * @return QueryWrapper instance
     */
    QueryWrapper getQueryWrapper();

}
