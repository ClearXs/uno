package cc.allio.uno.data.orm.dsl.type;

import cc.allio.uno.core.type.Types;

/**
 * short java type
 *
 * @author jiangwei
 * @date 2023/1/13 18:00
 * @since 1.1.4
 */
public class ShortJavaType implements JavaType<Short> {

    @Override
    public Class<Short> getJavaType() {
        return Types.SHORT;
    }

    @Override
    public boolean equalsTo(Class<?> otherJavaType) {
        return Short.class.isAssignableFrom(otherJavaType)
                || short.class.isAssignableFrom(otherJavaType);
    }
}
