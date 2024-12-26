package cc.allio.uno.data.query.db.mapper;

import cc.allio.uno.data.query.HigherQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 通用查询功能
 *
 * @author j.x
 * @since 1.0
 */
public interface QueryMapper<T> extends BaseMapper<T>, HigherQuery {

}
