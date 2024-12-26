package cc.allio.uno.gis.local.type;

import cc.allio.uno.data.orm.dsl.type.JavaTypeImpl;
import cc.allio.uno.gis.GeometryTypes;
import org.locationtech.jts.geom.LineString;

/**
 * LineString
 *
 * @author j.x
 * @since 1.1.4
 */
public class LineStringJavaType extends JavaTypeImpl<LineString> {
    @Override
    public Class<LineString> getJavaType() {
        return GeometryTypes.LINE_STRING;
    }

    @Override
    public boolean equalsTo(Class<?> other) {
        return GeometryTypes.LINE_STRING.isAssignableFrom(other);
    }
}
