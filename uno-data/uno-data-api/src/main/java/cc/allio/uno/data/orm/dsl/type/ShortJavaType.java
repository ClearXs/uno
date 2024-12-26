package cc.allio.uno.data.orm.dsl.type;

import cc.allio.uno.core.type.Types;

/**
 * short java type
 *
 * @author j.x
 * @since 1.1.4
 */
public class ShortJavaType implements JavaType<Short> {

    @Override
    public Class<Short> getJavaType() {
        return Types.SHORT;
    }

    @Override
    public boolean equalsTo(Class<?> otherJavaType) {
        if (otherJavaType == null) {
            return false;
        }
        if (otherJavaType.isPrimitive()) {
            return short.class.isAssignableFrom(otherJavaType);
        }
        return Short.class.isAssignableFrom(otherJavaType);
    }
}
