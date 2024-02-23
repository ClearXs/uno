package cc.allio.uno.gis.local.type;

import cc.allio.uno.data.orm.dsl.type.JavaTypeImpl;
import cc.allio.uno.gis.GeometryTypes;
import org.locationtech.jts.geom.MultiPoint;

/**
 * MultiPoint
 *
 * @author jiangwei
 * @date 2023/4/18 16:00
 * @since 1.1.4
 */
public class MultiPointJavaType extends JavaTypeImpl<MultiPoint> {
    @Override
    public Class<MultiPoint> getJavaType() {
        return GeometryTypes.MULTI_POINT;
    }

    @Override
    public boolean equalsTo(Class<?> other) {
        return GeometryTypes.MULTI_POINT.isAssignableFrom(other);
    }
}
