package cc.allio.uno.data.query.mybatis;

/**
 * QueryFilter
 *
 * @author jiangwei
 * @date 2023/4/17 18:11
 * @since 1.1.4
 */
public interface QueryFilter {

    /**
     * 获取查询的sql
     *
     * @return sql
     */
    String getSQL();

    /**
     * set query wrapper
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
