package cc.allio.uno.gis.transform;

import cc.allio.uno.gis.SRID;
import org.springframework.util.ClassUtils;

import java.lang.annotation.*;
import java.lang.reflect.Constructor;

/**
 * 坐标系转换关系
 *
 * @author jiangwei
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

    interface Builder {

        /**
         * 创建{@link CrsTransform}对象
         *
         * @param fromTo FromTo实体
         * @return CrsTransform or null
         */
        static CrsTransform make(FromTo fromTo) {
            Class<? extends CrsTransform> clazz = fromTo.transform();
            Constructor<? extends CrsTransform> constructor = ClassUtils.getConstructorIfAvailable(clazz, fromTo.fromCrs().getClass(), fromTo.toCrs().getClass());
            if (constructor != null) {
                try {
                    return constructor.newInstance(fromTo.fromCrs(), fromTo.toCrs());
                } catch (Throwable ex) {
                    // ignore
                }
            }
            return null;
        }
    }
}