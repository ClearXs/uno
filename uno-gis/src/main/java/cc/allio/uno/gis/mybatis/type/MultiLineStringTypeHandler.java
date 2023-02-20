package cc.allio.uno.gis.mybatis.type;

import cc.allio.uno.gis.mybatis.MybatisProperties;
import org.apache.ibatis.type.MappedTypes;
import org.locationtech.jts.geom.MultiLineString;

@MappedTypes(MultiLineString.class)
public class MultiLineStringTypeHandler extends AbstractGeometryTypeHandler<MultiLineString> {
    public MultiLineStringTypeHandler(MybatisProperties mybatisProperties) {
        super(mybatisProperties);
    }
}
