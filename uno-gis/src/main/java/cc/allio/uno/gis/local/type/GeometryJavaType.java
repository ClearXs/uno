package cc.allio.uno.gis.local.type;

import cc.allio.uno.data.orm.dsl.type.JavaTypeImpl;
import cc.allio.uno.gis.GeometryTypes;
import org.locationtech.jts.geom.Geometry;

/**
 * Geometry
 *
 * @author jiangwei
 * @date 2023/4/18 16:08
 * @since 1.1.4
 */
public class GeometryJavaType extends JavaTypeImpl<Geometry> {
    @Override
    public Class<Geometry> getJavaType() {
        return GeometryTypes.GEOMETRY;
    }

    @Override
    public boolean equalsTo(Class<?> other) {
        return GeometryTypes.isGeometry(other);
    }
}
