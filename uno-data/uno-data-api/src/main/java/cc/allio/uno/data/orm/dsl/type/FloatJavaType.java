package cc.allio.uno.data.orm.dsl.type;

import cc.allio.uno.core.type.Types;

/**
 * Float
 *
 * @author j.x
 * @since 1.1.4
 */
public class FloatJavaType extends JavaTypeImpl<Float> {

    @Override
    public Class<Float> getJavaType() {
        return Types.FLOAT;
    }

    @Override
    public boolean equalsTo(Class<?> otherJavaType) {
        if (otherJavaType == null) {
            return false;
        }
        if (otherJavaType.isPrimitive()) {
            return float.class.isAssignableFrom(otherJavaType);
        }
        return Float.class.isAssignableFrom(otherJavaType);
    }
}
