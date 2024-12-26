package cc.allio.uno.data.orm.dsl.type;

import cc.allio.uno.core.type.Types;

import java.util.Set;

/**
 * {@link Set}
 *
 * @author j.x
 * @since 1.1.4
 */
public class SetJavaType extends JavaTypeImpl<Set> {

    @Override
    public Class<Set> getJavaType() {
        return Types.SET;
    }

    @Override
    public boolean equalsTo(Class<?> otherJavaType) {
        return Set.class.isAssignableFrom(otherJavaType);
    }
}
