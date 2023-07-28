package cc.allio.uno.gis.local.type;

import cc.allio.uno.data.orm.type.JavaTypeImpl;
import cc.allio.uno.gis.GeometryTypes;
import org.locationtech.jts.geom.Polygon;

/**
 * Polygon
 *
 * @author jiangwei
 * @date 2023/4/18 16:02
 * @since 1.1.4
 */
public class PolygonJavaType extends JavaTypeImpl<Polygon> {
    @Override
    public Class<Polygon> getJavaType() {
        return GeometryTypes.POLYGON;
    }
}
