package cc.allio.uno.data.orm.dsl.type;

import cc.allio.uno.core.type.Types;

/**
 * Integer
 *
 * @author jiangwei
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
        return Integer.class.isAssignableFrom(otherJavaType)
                || int.class.isAssignableFrom(otherJavaType);
    }
}
