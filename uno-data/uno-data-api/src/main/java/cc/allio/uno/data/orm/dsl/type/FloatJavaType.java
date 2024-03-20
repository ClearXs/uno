package cc.allio.uno.data.orm.dsl.type;

import cc.allio.uno.core.type.Types;

/**
 * Float
 *
 * @author j.x
 * @date 2023/4/14 18:45
 * @since 1.1.4
 */
public class FloatJavaType extends JavaTypeImpl<Float> {

    @Override
    public Class<Float> getJavaType() {
        return Types.FLOAT;
    }

    @Override
    public boolean equalsTo(Class<?> otherJavaType) {
        return Float.class.isAssignableFrom(otherJavaType)
                || float.class.isAssignableFrom(otherJavaType);
    }
}
