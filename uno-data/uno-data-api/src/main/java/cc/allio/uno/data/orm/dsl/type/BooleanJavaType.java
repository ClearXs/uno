package cc.allio.uno.data.orm.dsl.type;

import cc.allio.uno.core.type.Types;

/**
 * boolean java type
 *
 * @author j.x
 * @since 1.1.4
 */
public class BooleanJavaType extends JavaTypeImpl<Boolean> {

    @Override
    public Class<Boolean> getJavaType() {
        return Types.BOOLEAN;
    }

    @Override
    public boolean equalsTo(Class<?> otherJavaType) {
        if (otherJavaType == null) {
            return false;
        }
        if (otherJavaType.isPrimitive()) {
            return boolean.class.isAssignableFrom(otherJavaType);
        }
        return Boolean.class.isAssignableFrom(otherJavaType);
    }
}
