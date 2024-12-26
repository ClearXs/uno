package cc.allio.uno.gis;

import cc.allio.uno.core.type.Types;
import com.google.common.collect.Sets;
import org.locationtech.jts.geom.*;

import java.util.Set;

/**
 * 空间数据类型
 *
 * @author j.x
 * @since 1.1.2
 */
public final class GeometryTypes extends Types {

    public static final Class<Geometry> GEOMETRY = Geometry.class;
    public static final Class<Point> POINT = Point.class;
    public static final Class<LineString> LINE_STRING = LineString.class;
    public static final Class<Polygon> POLYGON = Polygon.class;
    public static final Class<LinearRing> LINEAR_RING = LinearRing.class;
    public static final Class<MultiLineString> MULTI_LINE_STRING = MultiLineString.class;
    public static final Class<MultiPoint> MULTI_POINT = MultiPoint.class;
    public static final Class<MultiPolygon> MULTI_POLYGON = MultiPolygon.class;
    public static final Class<GeometryCollection> GEOMETRY_COLLECTION = GeometryCollection.class;

    public static final int GEOMETRY_CODE = 10000;
    public static final int POINT_CODE = 10001;
    public static final int LINE_STRING_CODE = 10002;
    public static final int POLYGON_CODE = 10003;
    public static final int LINEAR_RING_CODE = 10004;
    public static final int MULTI_LINE_STRING_CODE = 10005;
    public static final int MULTI_POINT_CODE = 10006;
    public static final int MULTI_POLYGON_CODE = 10007;
    public static final int GEOMETRY_COLLECTION_CODE = 10008;


    private static final Set<Class<? extends Geometry>> GEOMETRY_TYPES = Sets.newHashSet(GEOMETRY, POINT, LINE_STRING, POLYGON, LINEAR_RING, MULTI_POINT, MULTI_LINE_STRING, MULTI_POLYGON, GEOMETRY_COLLECTION);

    /**
     * 判断给定的class对象
     *
     * @param clazz clazz对象
     * @return
     */
    public static boolean isGeometry(Class<?> clazz) {
        return GEOMETRY_TYPES.contains(clazz);
    }
}
