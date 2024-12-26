package cc.allio.uno.gis.mybatis;

import cc.allio.uno.gis.transform.FromTo;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * mybatis 数据库坐标转换，用于实体上。
 *
 * @author j.x
 * @since 1.1.2
 */
@Documented
@Target({TYPE})
@Retention(RUNTIME)
public @interface MybatisTransform {

    /**
     * 数据库读数据时应用
     *
     * @return 默认srid = 4326的坐标转换
     */
    FromTo read() default @FromTo;

    /**
     * 数据写数据时应用
     *
     * @return 默认srid = 4326的坐标转换
     */
    FromTo write() default @FromTo;
}
