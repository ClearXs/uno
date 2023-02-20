package cc.allio.uno.gis.transform;

import org.locationtech.jts.geom.Geometry;

/**
 * 读CrsTransform 标识接口
 *
 * @author jiangwei
 * @date 2022/12/8 19:37
 * @since 1.1.2
 */
class ReadableCrsTransform implements CrsTransform {

    private final CrsTransform transform;

    ReadableCrsTransform(CrsTransform transform) {
        this.transform = transform;
    }

    @Override
    public <T extends Geometry> T transform(T fromGeometry) {
        return transform.transform(fromGeometry);
    }
}
