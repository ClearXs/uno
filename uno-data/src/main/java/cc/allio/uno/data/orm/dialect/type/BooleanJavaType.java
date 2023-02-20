package cc.allio.uno.data.orm.dialect.type;

import cc.allio.uno.core.util.type.Types;

/**
 * boolean java type
 *
 * @author jiangwei
 * @date 2023/1/16 09:49
 * @since 1.1.4
 */
public class BooleanJavaType extends JavaTypeImpl<Boolean> {
    @Override
    public Class<Boolean> getJavaType() {
        return Types.BOOLEAN;
    }
}
