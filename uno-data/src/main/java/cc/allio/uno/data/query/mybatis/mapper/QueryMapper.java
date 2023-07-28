package cc.allio.uno.data.query.mybatis.mapper;

import cc.allio.uno.data.query.mybatis.HigherQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 通用查询功能
 *
 * @author jiangwei
 * @date 2022/9/28 10:28
 * @since 1.0
 */
public interface QueryMapper<T> extends BaseMapper<T>, HigherQuery {

}
