package cc.allio.uno.gis.transform;

import cc.allio.uno.gis.SRID;

import java.lang.annotation.*;

/**
 * 坐标系转换关系
 *
 * @author j.x
 * @date 2022/12/8 20:09
 * @since 1.1.2
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FromTo {

    /**
     * 来源坐标系
     *
     * @return 坐标CRS Code
     */
    SRID fromCrs() default SRID.WGS84_4326;

    /**
     * 转换后的坐标系
     *
     * @return 坐标CRS Code
     */
    SRID toCrs() default SRID.WGS84_4326;

    /**
     * 给定坐标转换类型
     *
     * @return
     */
    Class<? extends CrsTransform> transform() default JTSCrsTransform.class;
}