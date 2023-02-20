package cc.allio.uno.gis.transform;

import com.google.common.collect.Lists;
import org.locationtech.jts.geom.Geometry;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;

/**
 * 根据对象上标识的注解，寻找适合于它的{@link CrsTransform}实例
 *
 * @author jiangwei
 * @date 2022/12/8 20:25
 * @see FromTo
 * @since 1.1.2
 */
public class AnnoTransform implements CrsTransform {

    private static final List<String> READ_ANNOTATIONS = Lists.newArrayList("serializer", "read");
    private static final List<String> WRITE_ANNOTATIONS = Lists.newArrayList("deserializer", "write");

    private Optional<CrsTransform> oTrans = Optional.empty();

    public AnnoTransform(Object o, Class<? extends Annotation> classAnno, ReadWrite readWrite) {
        this(Optional.ofNullable(o).map(Object::getClass).orElse(null), classAnno, readWrite);
    }

    public AnnoTransform(Class<?> clazz, Class<? extends Annotation> classAnno, ReadWrite readWrite) {
        try {
            oTrans = Optional.ofNullable(clazz)
                    .flatMap(notnull -> Optional.ofNullable(AnnotationUtils.findAnnotation(clazz, classAnno)))
                    .map(AnnotationUtils::getAnnotationAttributes)
                    .flatMap(attributes -> {
                        if (readWrite == ReadWrite.READ) {
                            return READ_ANNOTATIONS.stream()
                                    .map(attributes::get)
                                    .findFirst();

                        } else if (readWrite == ReadWrite.WRITE) {
                            return WRITE_ANNOTATIONS.stream()
                                    .map(attributes::get)
                                    .findFirst();
                        }
                        return Optional.empty();
                    })
                    .map(FromTo.class::cast)
                    .flatMap(anno -> Optional.ofNullable(FromTo.Builder.make(anno)))
                    .map(t -> {
                        if (ReadWrite.READ == readWrite) {
                            return t.createReadable();
                        } else if (ReadWrite.WRITE == readWrite) {
                            return t.createWritable();
                        } else {
                            return t.createReadWrite();
                        }
                    });
        } catch (Throwable ex) {
            // ignore
        }
    }

    @Override
    public <T extends Geometry> T transform(T fromGeometry) {
        return oTrans.map(transform -> transform.transform(fromGeometry))
                .orElse(fromGeometry);
    }
}
