package cc.allio.uno.gis.mybatis.type;

import cc.allio.uno.gis.mybatis.MybatisProperties;
import org.apache.ibatis.type.MappedTypes;
import org.locationtech.jts.geom.Polygon;

@MappedTypes(Polygon.class)
public class PolygonTypeHandler extends AbstractGeometryTypeHandler<Polygon> {
    public PolygonTypeHandler(MybatisProperties mybatisProperties) {
        super(mybatisProperties);
    }
}
