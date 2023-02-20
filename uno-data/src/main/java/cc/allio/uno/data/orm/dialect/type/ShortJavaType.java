package cc.allio.uno.data.orm.dialect.type;

import cc.allio.uno.core.util.type.Types;

/**
 * short java type
 *
 * @author jiangwei
 * @date 2023/1/13 18:00
 * @since 1.1.4
 */
public class ShortJavaType implements JavaType<Short> {

    @Override
    public Class<Short> getJavaType() {
        return Types.SHORT;
    }
}
