package cc.allio.uno.data.orm.dsl.type;

import cc.allio.uno.core.api.Adapter;

/**
 * SQL 数据库类型适配器
 *
 * @author j.x
 * @date 2023/4/13 13:17
 * @since 1.1.4
 */
public interface DBTypeAdapter<T> extends Adapter<T, DBType> {
}
