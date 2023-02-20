package cc.allio.uno.gis.transform;

import org.locationtech.jts.geom.Geometry;

/**
 * 坐标转换
 *
 * @author jiangwei
 * @date 2022/12/8 11:13
 * @see BaseCrsTransform
 * @see JTSCrsTransform
 * @see FromTo
 * @since 1.1.2
 */
public interface CrsTransform {

    String EPSG = "EPSG";
    String SRID = "SRID";

    /**
     * 根据给定的目标坐标系进行坐标转换
     *
     * @param fromGeometry 未进行坐标转换数据
     * @param <T>          extend Geometry
     * @return 坐标转换完成的空间数据
     */
    <T extends Geometry> T transform(T fromGeometry);

    /**
     * 创建读坐标转换
     *
     * @return
     */
    default ReadableCrsTransform createReadable() {
        return new ReadableCrsTransform(this);
    }

    /**
     * 创建写坐标转换
     *
     * @return
     */
    default WritableCrsTransform createWritable() {
        return new WritableCrsTransform(this);
    }

    /**
     * 创建读写坐标转换
     *
     * @return
     */
    default ReadWriteCrsTransform createReadWrite() {
        return new ReadWriteCrsTransform(this);
    }
}
