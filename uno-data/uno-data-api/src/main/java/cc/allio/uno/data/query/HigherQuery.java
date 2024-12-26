package cc.allio.uno.data.query;

import cc.allio.uno.data.query.stream.ValueTime;

import java.util.Collection;
import java.util.Map;

/**
 * 复合的高阶查询，提供对单表数据多维度的快速分析
 *
 * @author j.x
 * @see BaseHigherQuery
 * @since 1.1.4
 */
public interface HigherQuery {

    /**
     * 基于查询过滤条件，使查询语句满足给定的数据
     *
     * @param queryFilter 查询条件
     * @return List
     */
    @Query
    <T> Collection<T> queryList(QueryFilter queryFilter) throws Throwable;

    /**
     * 查询同期对比数据
     *
     * @param queryFilter 查询条件
     * @return Map k 时间 v List
     */
    @Query
    <T> Map<String, Collection<?>> queryContemporaneous(QueryFilter queryFilter) throws Throwable;

    /**
     * 把通过{@link #queryList(QueryFilter)}的数据转换为{@link ValueTime}的List数据
     *
     * @param queryFilter 查询条件
     * @return List ValueTime数据 转换为当前结构：test:[{"time":"xxxx","value":"test"}] ...
     */
    @Query
    Map<String, Collection<ValueTime>> queryListForValueTime(QueryFilter queryFilter) throws Throwable;

    /**
     * 把通过{@link #queryContemporaneous(QueryFilter)}的数据转换为{@link ValueTime}的List数据
     *
     * @param queryFilter 查询条件
     * @return Map k 时间 v List ValueTime数据
     */
    @Query
    Map<String, Map<String, Collection<ValueTime>>> queryContemporaneousForValueTime(QueryFilter queryFilter) throws Throwable;
}
