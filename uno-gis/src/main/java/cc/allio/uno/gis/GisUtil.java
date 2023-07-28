package cc.allio.uno.gis;

import cc.allio.uno.gis.transform.CrsTransform;
import cc.allio.uno.gis.transform.CrsTransformBuilder;
import cc.allio.uno.gis.transform.JTSCrsTransform;
import lombok.NonNull;
import org.geotools.geojson.geom.GeometryJSON;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;

/**
 * Gis 实用工具集
 *
 * @author jiangwei
 * @date 2023/2/27 16:18
 * @since 1.1.3
 */
public class GisUtil {

    private GisUtil() {
    }

    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();

    /**
     * 计算给定点位的高程数据
     *
     * @param point   点位数据
     * @param tifPath .tif文件路径
     * @return 高晨数据
     */
    public static BigDecimal elevation(@NonNull Point point, String tifPath) {
        // TODO 需要加载DDL文件 https://www.cnblogs.com/unlockth/p/14062076.html
        return BigDecimal.ZERO;
    }

    /**
     * 根据指定的x，y坐标创建Point实例对象
     *
     * @param x x
     * @param y y
     * @return Point 实例
     */
    public static Point createPoint(double x, double y) {
        return createPoint(x, y, Coordinate.NULL_ORDINATE);
    }

    /**
     * 根据指定的x，y坐标、srid创建Point实例对象
     *
     * @param x    x
     * @param y    y
     * @param srid srid
     * @return Point 实例
     */
    public static Point createPoint(double x, double y, SRID srid) {
        return createPoint(x, y, Coordinate.NULL_ORDINATE, srid);
    }

    /**
     * 根据指定的x，y，z坐标创建Point实例对象
     *
     * @param x x
     * @param y y
     * @param z z
     * @return Point 实例
     */
    public static Point createPoint(double x, double y, double z) {
        return createPoint(x, y, z, null);
    }


    /**
     * 根据指定的x，y，z坐标 srid创建Point实例对象
     *
     * @param x    x
     * @param y    y
     * @param z    z
     * @param srid srid
     * @return Point 实例
     */
    public static Point createPoint(double x, double y, double z, SRID srid) {
        return createPoint(new Coordinate(x, y, z), srid);
    }


    public static Point createPoint(Coordinate coordinate, SRID srid) {
        Point point = GEOMETRY_FACTORY.createPoint(coordinate);
        if (srid != null) {
            point.setSRID(srid.getCode());
        }
        return point;
    }

    /**
     * 坐标转换
     *
     * @param geometry 空间数据
     * @param to       目标坐标系
     * @param <T>      Geometry
     */
    public static <T extends Geometry> T transform(T geometry, SRID to) {
        int fromSRID = geometry.getSRID();
        SRID from = SRID.from(fromSRID);
        CrsTransform transform = CrsTransformBuilder.make().buildTransformClazz(JTSCrsTransform.class).buildFromSRID(from).buildToSRID(to).build();
        return transform.transform(geometry);
    }

    /**
     * JTS对象转json
     * @param geometry
     * @return
     * @throws IOException
     */
    public static String wktToJson(Geometry geometry) throws IOException {
        StringWriter writer = new StringWriter();
        GeometryJSON g = new GeometryJSON(20);
        g.write(geometry, writer);
        String json = writer.toString();
        return json;
    }

    /**
     * json对象转JTS
     * @param json
     * @return
     * @throws IOException
     */
    public static Geometry jsonToWkt(String json) throws IOException {
        StringReader reader = new StringReader(json);
        GeometryJSON g = new GeometryJSON(20);
        Geometry geometry =  g.read(reader);
        return geometry;
    }
}
