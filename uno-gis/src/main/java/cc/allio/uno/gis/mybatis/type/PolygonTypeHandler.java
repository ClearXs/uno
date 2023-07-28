package cc.allio.uno.gis.mybatis.type;

import cc.allio.uno.gis.config.UnoGisProperties;
import org.apache.ibatis.type.MappedTypes;
import org.locationtech.jts.geom.Polygon;

@MappedTypes(Polygon.class)
public class PolygonTypeHandler extends AbstractGeometryTypeHandler<Polygon> {
    public PolygonTypeHandler(UnoGisProperties gisProperties) {
        super(gisProperties);
    }
}
