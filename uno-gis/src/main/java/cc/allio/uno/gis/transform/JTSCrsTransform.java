package cc.allio.uno.gis.transform;

import cc.allio.uno.gis.SRID;
import lombok.extern.slf4j.Slf4j;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Geometry;

/**
 * JTS坐标系转换
 *
 * @author jiangwei
 * @date 2022/12/8 11:24
 * @since 1.1.2
 */
@Slf4j
public class JTSCrsTransform extends BaseCrsTransform {

    public JTSCrsTransform(SRID fromCrs, SRID toCrs) throws FactoryException {
        super(fromCrs, toCrs);
    }

    @Override
    public <T extends Geometry> T transform(T fromGeometry) {
        MathTransform mathTransform = null;
        try {
            mathTransform = CRS.findMathTransform(getFromCrs(), getToCrs(), true);
            return (T) JTS.transform(fromGeometry, mathTransform);
        } catch (Throwable ex) {
            // record ex
            log.error("jts transform failed", ex);
            return fromGeometry;
        }
    }
}
