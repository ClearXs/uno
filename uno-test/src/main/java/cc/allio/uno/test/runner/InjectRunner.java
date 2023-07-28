package cc.allio.uno.test.runner;

import cc.allio.uno.core.util.ReflectUtils;
import cc.allio.uno.test.CoreTest;
import cc.allio.uno.test.Inject;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

/**
 * 用于被{@link Inject}注解标识的字段进行注入.通过检查测试类声明的字段上是否包含{@link Inject}注解，如果包含则从Spring环境中获取该字段类型的Bean对象进行注入。
 * <p>
 * update: 1.1.4版本后{@link Autowired}、{@link Resource}注解也可以用于bean对象注入
 * </p>
 *
 * @author jiangwei
 * @date 2022/10/28 17:43
 * @since 1.1.0
 */
public class InjectRunner implements RefreshCompleteRunner {

    /**
     * 存储可以被用于注入的注解
     */
    private final Set<Class<? extends Annotation>> injectAnnos;

    public InjectRunner() {
        this.injectAnnos = Sets.newHashSet();
        addInjectableIdentity(Inject.class);
        addInjectableIdentity(Autowired.class);
        addInjectableIdentity(Resource.class);
    }

    @Override
    public void onRefreshComplete(CoreTest coreTest) throws Throwable {
        Class<?> testClass = coreTest.getTestClass();
        Arrays.stream(testClass.getDeclaredFields())
                .filter(field -> injectAnnos.stream().anyMatch(field::isAnnotationPresent))
                .forEach(field -> {
                    ReflectUtils.makeAccessible(field);
                    ReflectUtils.setField(field, coreTest.getTestInstance(), coreTest.getBean(field.getType()));
                });
    }

    /**
     * 添加可以被注入注解的标识
     *
     * @param injectAnno injectAnno
     */
    public void addInjectableIdentity(Class<? extends Annotation> injectAnno) {
        injectAnnos.add(injectAnno);
    }
}
