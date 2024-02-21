package cc.allio.uno.data.orm.dsl.dialect;

import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.dsl.type.DSLType;

/**
 * 不同数据类型方言转换器
 *
 * @author jiangwei
 * @date 2024/1/8 19:33
 * @since 1.1.6
 */
public interface TypeTranslator {

    /**
     * 类型转换
     *
     * @param dslType 原始类型
     * @return 翻译后的类型
     */
    DSLType translate(DSLType dslType);

    /**
     * 类型转换
     *
     * @param dslType   原始类型
     * @param precision precision
     * @param scale     scale
     * @return 翻译后的类型
     */
    DSLType translate(DSLType dslType, Integer precision, Integer scale);

    /**
     * 给定dsl type name 反转为{@link DSLType}实例
     * <p>基于{@link cc.allio.uno.data.orm.dsl.type.DSLType.DefaultDSLType}的数据进行获取</p>
     *
     * @param dslTypeName dslTypeName
     * @return DSLType instance if find
     */
    default DSLType reserve(String dslTypeName) {
        for (DSLType.DefaultDSLType dslType : DSLType.DefaultDSLType.values()) {
            if (dslType.getName().equalsIgnoreCase(dslTypeName)) {
                return translate(dslType);
            }
        }
        return null;
    }

    /**
     * 获取数据库类型
     */
    DBType getDBType();
}
