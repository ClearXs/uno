package cc.allio.uno.gis.mybatis.type;

import cc.allio.uno.gis.config.UnoGisProperties;
import org.apache.ibatis.type.MappedTypes;
import org.locationtech.jts.geom.LineString;

@MappedTypes(LineString.class)
public class LineStringTypeHandler extends AbstractGeometryTypeHandler<LineString> {

    public LineStringTypeHandler(UnoGisProperties gisProperties) {
        super(gisProperties);
    }
}
