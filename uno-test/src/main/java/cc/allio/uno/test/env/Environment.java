package cc.allio.uno.test.env;

import cc.allio.uno.test.CoreTest;
import cc.allio.uno.test.env.annotation.properties.AnnoPropertiesParser;
import cc.allio.uno.test.env.annotation.properties.Properties;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * 提供基础的测试环境：
 * <ul>
 *     <li>包含优先级关系</li>
 * </ul>
 *
 * @author j.x
 * @date 2022/2/14 14:05
 * @see AnnotationAwareOrderComparator#sort(List)
 * @since 1.1.1
 */
public interface Environment extends Ordered {

    /**
     * 提供环境支持
     *
     * @param coreTest core coreTest
     * @throws Throwable 提供环境过程中抛出异常
     */
    void support(CoreTest coreTest) throws Throwable;

    /**
     * 获取当前环境配置注解
     *
     * @return Annotation
     */
    Class<? extends Annotation>[] getPropertiesAnnotation();

    /**
     * 获取注解配置解析器
     *
     * @return AnnoPropertiesParser实例
     * @see Properties
     */
    default AnnoPropertiesParser getAnnoPropertiesParser() {
        return AnnoPropertiesParser.DEFAULT;
    }

    @Override
    default int getOrder() {
        return 0;
    }
}
