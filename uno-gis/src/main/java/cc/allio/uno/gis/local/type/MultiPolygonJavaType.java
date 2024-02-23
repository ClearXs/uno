package cc.allio.uno.gis.local.type;

import cc.allio.uno.data.orm.dsl.type.JavaTypeImpl;
import cc.allio.uno.gis.GeometryTypes;
import org.locationtech.jts.geom.MultiPolygon;

/**
 * MultiPolygon
 *
 * @author jiangwei
 * @date 2023/4/18 16:01
 * @since 1.1.4
 */
public class MultiPolygonJavaType extends JavaTypeImpl<MultiPolygon> {
    @Override
    public Class<MultiPolygon> getJavaType() {
        return GeometryTypes.MULTI_POLYGON;
    }

    @Override
    public boolean equalsTo(Class<?> other) {
        return GeometryTypes.MULTI_POLYGON.isAssignableFrom(other);
    }
}
