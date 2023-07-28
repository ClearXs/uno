package cc.allio.uno.data.orm.type;

import cc.allio.uno.core.type.Types;

/**
 * byte java type
 *
 * @author jiangwei
 * @date 2023/4/16 16:43
 * @since 1.1.4
 */
public class ByteJavaType extends JavaTypeImpl<Byte> {
    @Override
    public Class<Byte> getJavaType() {
        return Types.BYTE;
    }
}
