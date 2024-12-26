package cc.allio.uno.data.orm.dsl.type;

import java.util.Stack;

/**
 * {@link Stack}
 *
 * @author j.x
 * @since 1.1.4
 */
public class StackJavaType extends JavaTypeImpl<Stack> {
    @Override
    public Class<Stack> getJavaType() {
        return Stack.class;
    }

    @Override
    public boolean equalsTo(Class<?> otherJavaType) {
        return Stack.class.isAssignableFrom(otherJavaType);
    }
}
