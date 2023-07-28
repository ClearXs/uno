package cc.allio.uno.data.orm.type;

import cc.allio.uno.data.orm.SQLAdapter;

/**
 * 适用于不同框架的类型定义
 *
 * @author jiangwei
 * @date 2023/4/12 20:01
 * @since 1.1.4
 */
public interface DataTypeAdapter<T> extends SQLAdapter<T, DataType> {

}
