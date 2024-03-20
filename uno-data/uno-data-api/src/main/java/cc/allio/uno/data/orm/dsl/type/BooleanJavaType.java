package cc.allio.uno.data.orm.dsl.type;

import cc.allio.uno.core.type.Types;

/**
 * boolean java type
 *
 * @author j.x
 * @date 2023/1/16 09:49
 * @since 1.1.4
 */
public class BooleanJavaType extends JavaTypeImpl<Boolean> {
    @Override
    public Class<Boolean> getJavaType() {
        return Types.BOOLEAN;
    }

    @Override
    public boolean equalsTo(Class<?> otherJavaType) {
        return Boolean.class.isAssignableFrom(otherJavaType)
                || boolean.class.isAssignableFrom(otherJavaType);
    }
}
