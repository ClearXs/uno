package cc.allio.uno.gis.local.type;

import cc.allio.uno.data.orm.dsl.type.JavaTypeImpl;
import cc.allio.uno.gis.GeometryTypes;
import org.locationtech.jts.geom.Point;

/**
 * Point
 *
 * @author j.x
 * @since 1.1.4
 */
public class PointJavaType extends JavaTypeImpl<Point> {
    @Override
    public Class<Point> getJavaType() {
        return GeometryTypes.POINT;
    }

    @Override
    public boolean equalsTo(Class<?> other) {
        return GeometryTypes.POINT.isAssignableFrom(other);
    }
}
