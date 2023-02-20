package cc.allio.uno.data.mybatis.mapper;

import cc.allio.uno.data.query.Query;
import cc.allio.uno.data.query.QueryFilter;
import cc.allio.uno.data.query.stream.ValueTime;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Collection;
import java.util.Map;

/**
 * 通用查询功能
 *
 * @author jiangwei
 * @date 2022/9/28 10:28
 * @since 1.0
 */
public interface QueryMapper<T> extends BaseMapper<T> {

    /**
     * 基于查询过滤条件，使查询语句满足给定的数据
     *
     * @param queryFilter 查询条件
     * @return List
     */
    @Query
    Collection<T> queryList(QueryFilter queryFilter);

    /**
     * 查询同期对比数据
     *
     * @param queryFilter 查询条件
     * @return Map k 时间 v List
     */
    @Query
    Map<String, Collection<T>> queryContemporaneous(QueryFilter queryFilter);

    /**
     * 把通过{@link #queryList(QueryFilter)}的数据转换为{@link ValueTime}的List数据
     *
     * @param queryFilter 查询条件
     * @return List ValueTime数据 转换为当前结构：test:[{"time":"xxxx","value":"test"}] ...
     */
    @Query
    Map<String, Collection<ValueTime>> queryListForValueTime(QueryFilter queryFilter);

    /**
     * 把通过{@link #queryContemporaneous(QueryFilter)}的数据转换为{@link ValueTime}的List数据
     *
     * @param queryFilter 查询条件
     * @return Map k 时间 v List ValueTime数据
     */
    @Query
    Map<String, Map<String, Collection<ValueTime>>> queryContemporaneousForValueTime(QueryFilter queryFilter);
}
