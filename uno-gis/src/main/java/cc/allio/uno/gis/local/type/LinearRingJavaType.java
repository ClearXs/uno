package cc.allio.uno.gis.local.type;

import cc.allio.uno.data.orm.dsl.type.JavaTypeImpl;
import cc.allio.uno.gis.GeometryTypes;
import org.locationtech.jts.geom.LinearRing;

/**
 * LinearRing
 *
 * @author j.x
 * @date 2023/4/18 15:59
 * @since 1.1.4
 */
public class LinearRingJavaType extends JavaTypeImpl<LinearRing> {
    @Override
    public Class<LinearRing> getJavaType() {
        return GeometryTypes.LINEAR_RING;
    }

    @Override
    public boolean equalsTo(Class<?> other) {
        return GeometryTypes.LINEAR_RING.isAssignableFrom(other);
    }
}
