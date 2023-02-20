package cc.allio.uno.data.orm.dialect.type;

import cc.allio.uno.core.util.type.Types;

/**
 * long java type
 *
 * @author jiangwei
 * @date 2023/1/13 17:52
 * @since 1.1.4
 */
public class LongJavaType implements JavaType<Long> {

    @Override
    public Class<Long> getJavaType() {
        return Types.LONG;
    }
}
