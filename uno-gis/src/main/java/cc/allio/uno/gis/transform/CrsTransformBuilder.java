package cc.allio.uno.gis.transform;

import cc.allio.uno.gis.SRID;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Constructor;

/**
 * {@link CrsTransform}对象构造器
 *
 * @author j.x
 * @since 1.1.3
 */
public class CrsTransformBuilder {

    private Class<? extends CrsTransform> transformClazz;
    private SRID fromSRID;
    private SRID toSRID;

    private CrsTransformBuilder() {
    }

    /**
     * 创建一个新的{@link CrsTransformBuilder} 实例
     *
     * @return CrsTransformBuilder
     */
    public static CrsTransformBuilder make() {
        return new CrsTransformBuilder();
    }

    /**
     * 根据{@link FromTo}的参数创建{@link CrsTransformBuilder}
     *
     * @param fromTo fromTo
     * @return CrsTransformBuilder
     */
    public static CrsTransformBuilder make(FromTo fromTo) {
        CrsTransformBuilder crsTransformBuilder = new CrsTransformBuilder();
        crsTransformBuilder.buildTransformClazz(fromTo.transform());
        crsTransformBuilder.buildFromSRID(fromTo.fromCrs());
        crsTransformBuilder.buildToSRID(fromTo.toCrs());
        return crsTransformBuilder;
    }

    /**
     * 构建{@link CrsTransform} class对象
     *
     * @param clazz CrsTransform class
     * @return CrsTransformBuilder
     */
    public CrsTransformBuilder buildTransformClazz(Class<? extends CrsTransform> clazz) {
        this.transformClazz = clazz;
        return this;
    }

    /**
     * 构建来源的SRID
     *
     * @param fromSRID fromSRID
     * @return CrsTransformBuilder
     */
    public CrsTransformBuilder buildFromSRID(SRID fromSRID) {
        this.fromSRID = fromSRID;
        return this;
    }

    /**
     * 构建目标 SRID
     *
     * @param toSRID toSRID
     * @return CrsTransformBuilder
     */
    public CrsTransformBuilder buildToSRID(SRID toSRID) {
        this.toSRID = toSRID;
        return this;
    }

    /**
     * 构建CrsTransform 实例
     *
     * @return CrsTransform or null
     * @throws RuntimeException 创建实例失败时抛出异常
     */
    public CrsTransform build() {
        // 检查参数
        if (transformClazz == null) {
            throw new IllegalArgumentException("transformClazz must not null");
        }
        if (fromSRID == null) {
            throw new IllegalArgumentException("fromSRID must not null");
        }
        if (toSRID == null) {
            throw new IllegalArgumentException("toSRID must not null");
        }
        Constructor<? extends CrsTransform> constructor = ClassUtils.getConstructorIfAvailable(transformClazz, fromSRID.getClass(), toSRID.getClass());
        if (constructor != null) {
            try {
                return constructor.newInstance(fromSRID, toSRID);
            } catch (Throwable ex) {
                throw new RuntimeException(ex);
            }
        }
        return null;
    }
}
