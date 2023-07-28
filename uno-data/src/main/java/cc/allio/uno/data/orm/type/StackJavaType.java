package cc.allio.uno.data.orm.type;

import java.util.Stack;

/**
 * {@link Stack}
 *
 * @author jiangwei
 * @date 2023/4/16 17:09
 * @since 1.1.4
 */
public class StackJavaType extends JavaTypeImpl<Stack> {
    @Override
    public Class<Stack> getJavaType() {
        return Stack.class;
    }
}
