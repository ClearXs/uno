package cc.allio.uno.gis.mybatis.type;

import cc.allio.uno.gis.mybatis.MybatisProperties;
import org.apache.ibatis.type.MappedTypes;
import org.locationtech.jts.geom.MultiPolygon;

@MappedTypes(MultiPolygon.class)
public class MultiPolygonTypeHandler extends AbstractGeometryTypeHandler<MultiPolygon> {
    public MultiPolygonTypeHandler(MybatisProperties mybatisProperties) {
        super(mybatisProperties);
    }
}
