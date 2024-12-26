package cc.allio.uno.data.orm.dsl.type;

import cc.allio.uno.core.type.Types;

/**
 * Double
 *
 * @author j.x
 * @since 1.1.4
 */
public class DoubleJavaType extends JavaTypeImpl<Double> {
    @Override
    public Class<Double> getJavaType() {
        return Types.DOUBLE;
    }

    @Override
    public boolean equalsTo(Class<?> otherJavaType) {
        return Double.class.isAssignableFrom(otherJavaType)
                || double.class.isAssignableFrom(otherJavaType);
    }
}
