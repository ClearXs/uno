package cc.allio.uno.data.query;

import cc.allio.uno.data.query.param.DateDimension;
import cc.allio.uno.data.query.param.QuerySetting;
import reactor.util.function.Tuple2;

import java.io.Serializable;

/**
 * 查询领域对象
 *
 * @author jiangwei
 * @date 2022/9/29 12:52
 * @since 1.1.0
 */
public interface QueryWrapper extends Serializable {

    /**
     * 获取数据字段
     *
     * @return
     */
    String[] getDataFields();

    /**
     * 获取同期比较方式
     *
     * @return
     */
    DateDimension[] getContemporaneous();

    /**
     * 获取查询有关设置
     *
     * @return
     */
    QuerySetting getQuerySetting();

    /**
     * 获取当前查询时间字段标识
     *
     * @return 时间字段
     */
    String getTimeField();

    /**
     * 在同期对比方式中按照现有的QueryFilter构建新的QueryFilter实例，并按照同期时间放入查询中
     *
     * @return QueryFilter array = new
     */
    Tuple2<DateDimension, QueryFilter>[] groupByDateDimension();

    /**
     * 根据现有的QueryFilter实例创建新的QueryFilter
     *
     * @return QueryFilter
     */
    QueryFilter newQueryFilter();

    /**
     * 构建QueryFilter对象
     *
     * @return QueryFilter对象实例
     */
    QueryFilter build();

}
