package cc.allio.uno.data.orm.type;

import cc.allio.uno.core.type.Types;

import java.util.Set;

/**
 * {@link Set}
 *
 * @author jiangwei
 * @date 2023/4/16 17:07
 * @since 1.1.4
 */
public class SetJavaType extends JavaTypeImpl<Set> {

    @Override
    public Class<Set> getJavaType() {
        return Types.SET;
    }
}
