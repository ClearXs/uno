package cc.allio.uno.test.env.annotation.properties;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.time.Duration;
import java.util.Map;
import java.util.Set;

/**
 * 基于注解的Properties
 *
 * @author jiangwei
 * @date 2023/3/3 11:59
 * @since 1.1.4
 */
@FunctionalInterface
public interface AnnoPropertiesParser {

    AnnoPropertiesParser DEFAULT = new DefaultAnnoPropertiesParser();
    CombinePropertiesTypeParser COMBINE_PROPERTIES_TYPE_PARSER = new CombinePropertiesTypeParser();

    /**
     * 提取注解数据构建配置类
     *
     * @param annotation 注解数据
     * @return 配置数据
     */
    Map<String, Object> getProperties(Annotation annotation);

    /**
     * Properties Type 解析器
     */
    interface PropertiesTypeParser<T> {

        /**
         * 获取真实的Properties的对象
         */
        Object getRealProperties(Object fake);

        /**
         * 获取定义的类型
         *
         * @return Class
         */
        Class<T> getDefineType();

    }

    /**
     * 集合
     */
    class CombinePropertiesTypeParser {

        public final Set<PropertiesTypeParser<?>> propertiesTypeParsers = Sets.newHashSet();
        public final PropertiesTypeParser<?> defaultPropertiesTypeParser;

        CombinePropertiesTypeParser() {
            propertiesTypeParsers.add(new DurationPropertiesTypeParser());
            // 默认原始对象
            defaultPropertiesTypeParser = new PropertiesTypeParser<Object>() {
                @Override
                public Object getRealProperties(Object fake) {
                    return fake;
                }

                @Override
                public Class<Object> getDefineType() {
                    return null;
                }
            };

        }

        /**
         * 根据需求的类型获取配置类型解析器
         *
         * @param requireType requireType
         * @return PropertiesTypeParser or null
         */
        public PropertiesTypeParser<?> getPropertiesTypeParser(Class<?> requireType) {
            return propertiesTypeParsers.stream().filter(p -> p.getDefineType().equals(requireType)).findFirst().orElse(defaultPropertiesTypeParser);
        }

    }

    @Slf4j
    class DurationPropertiesTypeParser implements PropertiesTypeParser<Duration> {

        @Override
        public Object getRealProperties(Object fake) {
            try {
                return Duration.ofMillis((Long) fake);
            } catch (Throwable ex) {
                log.error("Duration type Parser failed ", ex);
            }
            return fake;
        }

        @Override
        public Class<Duration> getDefineType() {
            return Duration.class;
        }

    }
}
