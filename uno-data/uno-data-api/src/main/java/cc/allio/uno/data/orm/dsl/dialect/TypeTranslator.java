package cc.allio.uno.data.orm.dsl.dialect;

import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.dsl.type.DSLType;

/**
 * 不同数据库数据类型方言转换器
 *
 * @author jiangwei
 * @date 2024/1/8 19:33
 * @since 1.1.6
 */
public interface TypeTranslator {

    /**
     * 类型转换
     *
     * @param sqlType 原始类型
     * @return 翻译后的类型
     */
    DSLType translate(DSLType sqlType);

    /**
     * 获取数据库类型
     */
    DBType getDBType();
}
