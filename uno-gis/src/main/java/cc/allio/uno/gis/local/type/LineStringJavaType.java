package cc.allio.uno.gis.local.type;

import cc.allio.uno.data.orm.type.JavaTypeImpl;
import cc.allio.uno.gis.GeometryTypes;
import org.locationtech.jts.geom.LineString;

/**
 * LineString
 *
 * @author jiangwei
 * @date 2023/4/18 15:59
 * @since 1.1.4
 */
public class LineStringJavaType extends JavaTypeImpl<LineString> {
    @Override
    public Class<LineString> getJavaType() {
        return GeometryTypes.LINE_STRING;
    }
}
