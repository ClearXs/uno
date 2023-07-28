package cc.allio.uno.gis.mybatis.type;

import cc.allio.uno.gis.config.UnoGisProperties;
import org.apache.ibatis.type.MappedTypes;
import org.locationtech.jts.geom.LinearRing;

@MappedTypes(LinearRing.class)
public class LinearRingTypeHandler extends AbstractGeometryTypeHandler<LinearRing> {
    public LinearRingTypeHandler(UnoGisProperties gisProperties) {
        super(gisProperties);
    }
}
