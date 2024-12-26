package cc.allio.uno.gis.local.type;

import cc.allio.uno.data.orm.dsl.type.JavaTypeImpl;
import cc.allio.uno.gis.GeometryTypes;
import org.locationtech.jts.geom.Polygon;

/**
 * Polygon
 *
 * @author j.x
 * @since 1.1.4
 */
public class PolygonJavaType extends JavaTypeImpl<Polygon> {
    @Override
    public Class<Polygon> getJavaType() {
        return GeometryTypes.POLYGON;
    }

    @Override
    public boolean equalsTo(Class<?> other) {
        return GeometryTypes.POLYGON.isAssignableFrom(other);
    }
}
