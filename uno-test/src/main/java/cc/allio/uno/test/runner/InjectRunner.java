package cc.allio.uno.test.runner;

import cc.allio.uno.test.BaseCoreTest;
import cc.allio.uno.test.Inject;
import org.springframework.util.ReflectionUtils;

import java.util.Arrays;

/**
 * 用于被{@link Inject}注解标识的字段进行注入.通过检查测试类声明的字段上是否包含{@link Inject}注解，如果包含则从Spring环境中获取该字段类型的Bean对象进行注入
 *
 * @author jiangwei
 * @date 2022/10/28 17:43
 * @since 1.1.0
 */
public class InjectRunner implements RefreshCompleteRunner {

    @Override
    public void run(BaseCoreTest coreTest) throws Throwable {
        Arrays.stream(coreTest.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Inject.class))
                .forEach(field -> {
                    ReflectionUtils.makeAccessible(field);
                    ReflectionUtils.setField(field, coreTest, coreTest.getBean(field.getType()));
                });
    }
}
