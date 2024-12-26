package cc.allio.uno.data.orm.dsl.type;

import cc.allio.uno.core.type.Types;

/**
 * long java type
 *
 * @author j.x
 * @since 1.1.4
 */
public class LongJavaType implements JavaType<Long> {

    @Override
    public Class<Long> getJavaType() {
        return Types.LONG;
    }

    @Override
    public boolean equalsTo(Class<?> otherJavaType) {
        if (otherJavaType == null) {
            return false;
        }
        if (otherJavaType.isPrimitive()) {
            return long.class.isAssignableFrom(otherJavaType);
        }
        return Long.class.isAssignableFrom(otherJavaType);
    }
}
