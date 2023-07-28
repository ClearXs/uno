package cc.allio.uno.test.env;

import cc.allio.uno.core.util.ObjectUtils;
import cc.allio.uno.test.CoreTest;
import cc.allio.uno.test.env.annotation.properties.AnnoPropertiesParser;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

/**
 * 增加环境 Properties
 *
 * @author jiangwei
 * @date 2023/3/3 12:28
 * @since 1.1.4
 */
@Slf4j
public class PropertiesVisitor implements Visitor {

    @Override
    public void visit(CoreTest coreTest, Environment env) throws Throwable {
        Class<? extends Annotation>[] propertiesAnnos = env.getPropertiesAnnotation();
        if (ObjectUtils.isEmpty(propertiesAnnos)) {
            log.error("env [{}] not provided for properties annotation", env.getClass().getName());
            return;
        }
        for (Class<? extends Annotation> propertiesAnno : propertiesAnnos) {
            // 从Environment中给定环境所需要的注解配置，进行解析（该注解配置可能存在于测试类上也可能存在于指定的配置类上）
            parseAnnoProperties(propertiesAnno, coreTest, env.getAnnoPropertiesParser());
        }
    }

    /**
     * 解析Properties Annotation
     *
     * @param propertiesAnno       propertiesAnno
     * @param coreTest             core test
     * @param annoPropertiesParser 注解配置解析器
     */
    private void parseAnnoProperties(Class<? extends Annotation> propertiesAnno, CoreTest coreTest, AnnoPropertiesParser annoPropertiesParser) {
        if (coreTest.isAnnotation(propertiesAnno)) {
            Annotation annotation = coreTest.getAnnotation(propertiesAnno);
            if (annotation != null) {
                // 测试类或者配置类上存在
                Map<String, Object> properties = annoPropertiesParser.getProperties(annotation);
                for (Map.Entry<String, Object> kv : properties.entrySet()) {
                    String key = kv.getKey();
                    Object value = kv.getValue();
                    if (value instanceof List) {
                        coreTest.addPropertyWithList(key, (List<String>) value);
                    } else if (value instanceof Map) {
                        coreTest.addPropertyWithMap(key, (Map<String, String>) value);
                    } else if (value.getClass().isArray()) {
                        coreTest.addPropertyWithArray(key, (String[]) value);
                    } else {
                        coreTest.addProperty(key, value.toString());
                    }
                }
            }
        }
    }
}
