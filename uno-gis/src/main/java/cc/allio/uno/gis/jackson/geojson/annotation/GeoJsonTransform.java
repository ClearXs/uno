package cc.allio.uno.gis.jackson.geojson.annotation;

import cc.allio.uno.gis.transform.FromTo;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * GeoJson 坐标转换
 * <p>example</p>
 * <pre>
 *    &#064;GeoJson(type = GeoJsonType.FEATURE)
 *    &#064;GeoJsonTransform(type = GeoJsonType.FEATURE)
 *    &#064;JsonSerialize(using = GeoJsonSerializer.class)
 *    public class TestEntity {
 *
 *    }
 * </pre>
 *
 * @author j.x
 * @date 2022/12/8 10:58
 * @since 1.1.2
 */
@Documented
@Target({TYPE})
@Retention(RUNTIME)
public @interface GeoJsonTransform {

    /**
     * 序列化坐标转换
     *
     * @return
     */
    FromTo serializer() default @FromTo;

    /**
     * 反序列化坐标转换
     *
     * @return
     */
    FromTo deserializer() default @FromTo;


}
