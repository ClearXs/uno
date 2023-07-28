package cc.allio.uno.gis.local.type;

import cc.allio.uno.data.orm.type.JavaTypeImpl;
import cc.allio.uno.gis.GeometryTypes;
import org.locationtech.jts.geom.Point;

/**
 * Point
 *
 * @author jiangwei
 * @date 2023/4/18 16:01
 * @since 1.1.4
 */
public class PointJavaType extends JavaTypeImpl<Point> {
    @Override
    public Class<Point> getJavaType() {
        return GeometryTypes.POINT;
    }
}
