package cc.allio.uno.data.orm.dsl.type;

import cc.allio.uno.core.type.Types;

/**
 * long java type
 *
 * @author j.x
 * @date 2023/1/13 17:52
 * @since 1.1.4
 */
public class LongJavaType implements JavaType<Long> {

    @Override
    public Class<Long> getJavaType() {
        return Types.LONG;
    }

    @Override
    public boolean equalsTo(Class<?> otherJavaType) {
        return Long.class.isAssignableFrom(otherJavaType)
                || long.class.isAssignableFrom(otherJavaType);
    }
}
