package cc.allio.uno.data.orm.dsl.type;

import cc.allio.uno.core.type.Types;

/**
 * Integer
 *
 * @author j.x
 * @date 2023/1/13 16:08
 * @since 1.1.4
 */
public class IntegerJavaType extends JavaTypeImpl<Integer> {

    @Override
    public Class<Integer> getJavaType() {
        return Types.INTEGER;
    }

    @Override
    public boolean equalsTo(Class<?> otherJavaType) {
        if (otherJavaType == null) {
            return false;
        }
        if (otherJavaType.isPrimitive()) {
            return int.class.isAssignableFrom(otherJavaType);
        }
        return Integer.class.isAssignableFrom(otherJavaType);
    }
}
