package cc.allio.uno.data.orm.type;

import cc.allio.uno.core.type.Types;

import java.util.Map;

/**
 * map
 *
 * @author jiangwei
 * @date 2023/4/16 16:59
 * @since 1.1.4
 */
public class MapJavaType extends JavaTypeImpl<Map> {
    @Override
    public Class<Map> getJavaType() {
        return Types.MAP;
    }
}
