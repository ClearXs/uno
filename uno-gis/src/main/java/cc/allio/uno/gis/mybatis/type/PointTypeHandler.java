package cc.allio.uno.gis.mybatis.type;

import cc.allio.uno.gis.config.UnoGisProperties;
import org.apache.ibatis.type.MappedTypes;
import org.locationtech.jts.geom.Point;

@MappedTypes(Point.class)
public class PointTypeHandler extends AbstractGeometryTypeHandler<Point> {
    public PointTypeHandler(UnoGisProperties gisProperties) {
        super(gisProperties);
    }
}
