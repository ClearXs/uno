package cc.allio.uno.test.runner;

import cc.allio.uno.test.RunTest;
import cc.allio.uno.test.BaseCoreTest;
import cc.allio.uno.test.RunMapperTest;
import cc.allio.uno.test.RunServiceTest;
import cc.allio.uno.test.env.EmptyTestEnvironment;
import cc.allio.uno.test.env.TestSpringEnvironment;
import cc.allio.uno.test.env.TestSpringEnvironmentFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 注解{@link RunTest}的测试环境构建类
 *
 * @author jiangwei
 * @date 2022/10/28 16:35
 * @since 1.1.0
 */
@Slf4j
public class CoreTestRunner implements RegisterRunner {

    @Override
    public void run(BaseCoreTest coreTest) throws Throwable {
        runRegisterEnv(coreTest);
        runRegisterComponent(coreTest);
    }

    /**
     * <b>执行支持的环境</b>
     * <pre>
     *    获取该测试类上是否包含{@link RunTest}注册、或者重写{@link BaseCoreTest#supportEnv()}方法。
     *    从这两者之间构建{@link TestSpringEnvironmentFacade}实例，并执行{@link TestSpringEnvironmentFacade#support(BaseCoreTest)}方法
     * </pre>
     */
    private void runRegisterEnv(BaseCoreTest coreTest) {
        Set<RunTest> mergedAnnotations = AnnotatedElementUtils.findAllMergedAnnotations(coreTest.getClass(), RunTest.class);
        List<RunTest.Environment> envs = mergedAnnotations.stream().flatMap(runTest -> Arrays.stream(runTest.envs())).collect(Collectors.toList());
        // 注解上获取环境
        Stream<TestSpringEnvironmentFacade> annotationEnv = Stream.of(new TestSpringEnvironmentFacade(
                envs.stream()
                        .map(annoEnv -> {
                            try {
                                Class<? extends TestSpringEnvironment> envClazz = annoEnv.env();
                                Class<?>[] classArgs = annoEnv.classArgs();
                                if (!ObjectUtils.isEmpty(classArgs)) {
                                    Constructor<?>[] constructors = envClazz.getConstructors();
                                    for (Constructor<?> constructor : constructors) {
                                        if (constructor.getParameterCount() == classArgs.length) {
                                            return constructor.newInstance(classArgs);
                                        }
                                    }
                                }
                                return envClazz.newInstance();
                            } catch (Throwable ex) {
                                // ignore record
                                log.error("builder env error", ex);
                            }
                            return new EmptyTestEnvironment();
                        })
                        .toArray(TestSpringEnvironment[]::new)));
        // 子类重写方法上获取
        Stream<TestSpringEnvironment> overrideEnv = Optional.ofNullable(coreTest.supportEnv())
                .map(Stream::of)
                .orElse(Stream.empty());
        TestSpringEnvironmentFacade env = Stream.concat(overrideEnv, annotationEnv)
                .reduce(new TestSpringEnvironmentFacade(), TestSpringEnvironmentFacade::concat, TestSpringEnvironmentFacade::concat);
        env.support(coreTest);
        coreTest.setEnv(env);
    }

    /**
     * <b>执行注册组件</b>
     * <pre>
     *     读取该单元测试类上的{@link RunTest#components()}，注册于Spring中
     * </pre>
     */
    private void runRegisterComponent(BaseCoreTest coreTest) {
        Set<RunTest> mergedAnnotations = AnnotatedElementUtils.findAllMergedAnnotations(coreTest.getClass(), RunTest.class);
        List<Class<?>> components = mergedAnnotations.stream()
                .flatMap(runTest -> Arrays.stream(runTest.components()))
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(components)) {
            coreTest.registerComponent(components.toArray(new Class[]{}));
        }
        // RunMapperTest
        RunMapperTest mapperTest = AnnotationUtils.findAnnotation(coreTest.getClass(), RunMapperTest.class);
        if (!ObjectUtils.isEmpty(mapperTest)) {
            coreTest.registerComponent(mapperTest.mapperScan());
        }
        // RunServiceTest
        RunServiceTest serviceTest = AnnotationUtils.findAnnotation(coreTest.getClass(), RunServiceTest.class);
        if (!ObjectUtils.isEmpty(serviceTest)) {
            coreTest.registerComponent(serviceTest.mapperScan());
        }
    }

    @Override
    public boolean shared() {
        return false;
    }
}
