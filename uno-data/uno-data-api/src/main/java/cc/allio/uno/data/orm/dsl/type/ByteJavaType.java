package cc.allio.uno.data.orm.dsl.type;

import cc.allio.uno.core.type.Types;

/**
 * byte java type
 *
 * @author j.x
 * @date 2023/4/16 16:43
 * @since 1.1.4
 */
public class ByteJavaType extends JavaTypeImpl<Byte> {

    @Override
    public Class<Byte> getJavaType() {
        return Types.BYTE;
    }

    @Override
    public boolean equalsTo(Class<?> otherJavaType) {
        if (otherJavaType == null) {
            return false;
        }
        if (otherJavaType.isPrimitive()) {
            return byte.class.isAssignableFrom(otherJavaType);
        }
        return Byte.class.isAssignableFrom(otherJavaType);
    }
}
