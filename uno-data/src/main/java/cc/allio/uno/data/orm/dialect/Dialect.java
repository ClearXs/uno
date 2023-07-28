package cc.allio.uno.data.orm.dialect;

import cc.allio.uno.data.orm.dialect.func.FuncRegistry;
import cc.allio.uno.data.orm.type.TypeRegistry;

/**
 * 定义数据库方言对象
 *
 * @author jiangwei
 * @date 2023/1/5 18:27
 * @since 1.1.4
 */
public interface Dialect {

    /**
     * 获取数据库版本号
     *
     * @return Version实例
     */
    Version getVersion();

    /**
     * 获取Func registry
     *
     * @return FuncRegistry
     */
    FuncRegistry getFuncRegistry();

    /**
     * 获取Type registry
     *
     * @return TypeRegistry
     */
    TypeRegistry getTypeRegistry();
}
