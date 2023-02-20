package cc.allio.uno.gis.transform;

import org.locationtech.jts.geom.Geometry;

/**
 * 可写的CrsTransform
 *
 * @author jiangwei
 * @date 2022/12/8 19:38
 * @since 1.1.2
 */
class WritableCrsTransform implements CrsTransform {

    private final CrsTransform transform;

    WritableCrsTransform(CrsTransform transform) {
        this.transform = transform;
    }


    @Override
    public <T extends Geometry> T transform(T fromGeometry) {
        return transform.transform(fromGeometry);
    }
}
