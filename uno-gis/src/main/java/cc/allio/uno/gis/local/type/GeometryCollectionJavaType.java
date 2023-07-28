package cc.allio.uno.gis.local.type;

import cc.allio.uno.data.orm.type.JavaTypeImpl;
import cc.allio.uno.gis.GeometryTypes;
import org.locationtech.jts.geom.GeometryCollection;

/**
 * GeometryCollection
 *
 * @author jiangwei
 * @date 2023/4/18 16:11
 * @since 1.1.4
 */
public class GeometryCollectionJavaType extends JavaTypeImpl<GeometryCollection> {
    @Override
    public Class<GeometryCollection> getJavaType() {
        return GeometryTypes.GEOMETRY_COLLECTION;
    }
}
