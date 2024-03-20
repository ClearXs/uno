package cc.allio.uno.gis.transform;

import org.locationtech.jts.geom.Geometry;

/**
 * 可读（序列化）写（反序列化）的坐标转换
 *
 * @author j.x
 * @date 2022/12/8 18:27
 * @since 1.1.2
 */
class ReadWriteCrsTransform implements CrsTransform {

    private final CrsTransform transform;

    ReadWriteCrsTransform(CrsTransform transform) {
        this.transform = transform;
    }

    @Override
    public <T extends Geometry> T transform(T fromGeometry) {
        return transform.transform(fromGeometry);
    }
}
