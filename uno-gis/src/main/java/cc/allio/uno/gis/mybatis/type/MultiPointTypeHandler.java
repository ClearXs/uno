package cc.allio.uno.gis.mybatis.type;

import cc.allio.uno.gis.mybatis.MybatisProperties;
import org.apache.ibatis.type.MappedTypes;
import org.locationtech.jts.geom.MultiPoint;

@MappedTypes(MultiPoint.class)
public class MultiPointTypeHandler extends AbstractGeometryTypeHandler<MultiPoint> {

    public MultiPointTypeHandler(MybatisProperties mybatisProperties) {
        super(mybatisProperties);
    }
}
