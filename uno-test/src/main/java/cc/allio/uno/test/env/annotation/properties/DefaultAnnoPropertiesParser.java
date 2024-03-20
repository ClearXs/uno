package cc.allio.uno.test.env.annotation.properties;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.core.type.Types;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.NonNull;
import org.springframework.core.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 默认注解配置解析器。对应注解上必须含有{@link cc.allio.uno.test.env.annotation.properties.Properties}注解
 *
 * @author j.x
 * @date 2023/3/3 14:49
 * @since 1.1.4
 */
public class DefaultAnnoPropertiesParser implements AnnoPropertiesParser {

    // EMPTY标识
    private static final Object EMPTY = new Object();
    // 嵌入注解
    private final Set<Class<? extends Annotation>> embedAnnoCaches = Sets.newHashSet();

    @Override
    public Map<String, Object> getProperties(Annotation annotation) {
        try {
            return parseAnno(StringPool.EMPTY, annotation);
        } finally {
            embedAnnoCaches.clear();
        }
    }

    /**
     * 解析注解。获取注解上定义的方法，在获取对应方法上是否有{@link PropertiesName}、{@link PropertiesType}
     *
     * @param prefix     配置注解前缀
     * @param annotation annotation实例
     * @return Map<String, Object>
     */
    private Map<String, Object> parseAnno(String prefix, @NonNull Annotation annotation) {
        // 验证给定给的注解实例
        if (!validatePropertiesAnno(annotation)) {
            // 不包含@Properties注解
            return Collections.emptyMap();
        }
        // 构建当前注解实例
        AnnotationAttributes annotationAttributes = AnnotationUtils.getAnnotationAttributes(annotation, false, false);
        String identifiablePrefix = prefix;
        cc.allio.uno.test.env.annotation.properties.Properties properties = AnnotationUtils.findAnnotation(annotation.annotationType(), cc.allio.uno.test.env.annotation.properties.Properties.class);
        // 如果当前存在Properties注解并且prefix有值，那么则取该值
        if (properties != null && StringUtils.isNotBlank(properties.prefix())) {
            identifiablePrefix = properties.prefix();
        }
        Map<String, Object> kvProperties = Maps.newHashMap();
        Method[] methods = annotation.annotationType().getDeclaredMethods();
        for (Method method : methods) {
            String propertiesKey = method.getName();
            PropertiesName propertiesName = AnnotationUtils.findAnnotation(method, PropertiesName.class);
            if (propertiesName != null) {
                propertiesKey = propertiesName.value();
            }
            // 获取propertiesKey
            propertiesKey = identifiablePrefix + StringPool.ORIGIN_DOT + propertiesKey;
            Object propertiesValue = getPropertiesValue(annotation, method, propertiesKey, annotationAttributes);
            // 去除propertiesKey所标识的配置数据
            if (propertiesValue.equals(EMPTY)) {
                continue;
            }
            if (propertiesValue instanceof Map) {
                kvProperties.putAll((Map<? extends String, ?>) propertiesValue);
            } else {
                kvProperties.put(propertiesKey, propertiesValue);
            }
        }
        return kvProperties;
    }

    /**
     * 取每一个注解上的方法进行解析。有几种情况：
     * <ul>
     *     <li>方法上返回值本身就是Annotation实例，此时在调用{@link #parseAnno(String, Annotation)}进行解析</li>
     *     <li>方法上包含{@link AliasFor}注解，说明当前注解是一个嵌入注解，再次调用{@link #parseAnno(String, Annotation)}进行解析</li>
     *     <li>方法上包含{@link PropertiesType}注解，进行解析额</li>
     * </ul>
     *
     * @param annotation           当前注解annotation实例
     * @param method               注解上的某一个方法
     * @param propertiesKey        注解的配置key
     * @param annotationAttributes annotationAttributes
     * @return string or map or {@link #EMPTY}
     */
    private Object getPropertiesValue(Annotation annotation, Method method, String propertiesKey, AnnotationAttributes annotationAttributes) {
        String attributeName = method.getName();
        return Optional.ofNullable(annotationAttributes.get(attributeName))
                .map(v -> {
                    Object annoValue = v;
                    if (annoValue instanceof Annotation) {
                        return parseAnno(propertiesKey, (Annotation) annoValue);
                    }
                    // 判断指端字段上是否包含有@AliasFor注解，如果有则进行@SubProperties解析
                    AliasFor aliasFor = AnnotationUtils.findAnnotation(method, AliasFor.class);
                    if (aliasFor != null && !embedAnnoCaches.contains(aliasFor.annotation())) {
                        embedAnnoCaches.add(aliasFor.annotation());
                        Annotation embedAnno = MergedAnnotations.from(annotation).get(aliasFor.annotation()).synthesize();
                        return parseAnno(StringPool.EMPTY, embedAnno);
                    }
                    if (aliasFor != null && embedAnnoCaches.contains(aliasFor.annotation())) {
                        return EMPTY;
                    }
                    PropertiesType propertiesType = AnnotationUtils.findAnnotation(method, PropertiesType.class);
                    if (propertiesType != null) {
                        PropertiesTypeParser<?> propertiesTypeParser = COMBINE_PROPERTIES_TYPE_PARSER.getPropertiesTypeParser(annoValue.getClass());
                        annoValue = propertiesTypeParser.getRealProperties(annoValue);
                    }
                    Empty empty = AnnotationUtils.findAnnotation(method, Empty.class);
                    // 判断给定的注解方法上是否存在@Empty注解并且当前值为类型空值
                    if (empty != null && Types.isEmpty(annoValue)) {
                        return EMPTY;
                    }
                    return getStringValue(annoValue);
                })
                .orElse(EMPTY);
    }

    /**
     * 验证给定的注解实例上是否包含{@link cc.allio.uno.test.env.annotation.properties.Properties}注解
     *
     * @param annotation annotation实例
     * @return true 包含 false 不包含
     */
    private boolean validatePropertiesAnno(Annotation annotation) {
        return validateAnno(annotation, Properties.class);
    }

    /**
     * 判断给定注解实例上是否有需求的注解
     *
     * @param annotation annotation实例
     * @param annoType   annoType
     * @return treu 包含 false 不包含
     */
    private boolean validateAnno(Annotation annotation, Class<? extends Annotation> annoType) {
        return AnnotationUtils.isCandidateClass(annotation.annotationType(), annoType);
    }

    /**
     * 提取值的信息，构建成String or Map or List
     *
     * @return String or Map or List
     */
    private Object getStringValue(Object value) {
        if (value instanceof Class) {
            return ((Class<?>) value).getName();
        } else if (value instanceof Collection) {
            return ((Collection<?>) value).stream()
                    .map(this::getStringValue)
                    .collect(Collectors.toList());
        } else if (value instanceof Map) {
            return ((Map<?, ?>) value).entrySet().stream()
                    .collect(Collectors.toMap(kv -> getStringValue(kv.getKey()), kv -> getStringValue(kv.getValue())));
        } else if (value.getClass().isArray()) {
            return Stream.of((Object[]) value).map(Object::toString).toArray(String[]::new);
        }
        return value.toString();
    }


}
