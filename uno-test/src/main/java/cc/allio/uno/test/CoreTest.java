package cc.allio.uno.test;

import cc.allio.uno.test.env.Environment;
import cc.allio.uno.test.env.EnvironmentFacade;
import cc.allio.uno.test.env.Visitor;
import cc.allio.uno.test.runner.CoreRunner;
import cc.allio.uno.test.runner.Runner;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 提供uno.core下基础的Spring环境
 *
 * @author jiangwei
 * @date 2022/2/14 14:03
 * @since 1.0
 */
@Slf4j
public class CoreTest extends BaseSpringTest {

    // 当前进行测试的class对象
    private final Class<?> testClass;
    private EnvironmentFacade env;
    // 测试runner实例对象
    private CoreRunner coreRunner;
    // 构建{@link Environment}对象
    private Set<Visitor> visitors;

    public CoreTest() {
        this(null);
    }

    public CoreTest(Class<?> testClass) {
        if (testClass == null) {
            this.testClass = getClass();
        } else {
            this.testClass = testClass;
        }
    }

    // ----------------- lifecycle -----------------

    @Override
    protected void onInitSpringEnv() throws Throwable {
        Collection<Runner> registerRunner = getCoreRunner().getRegisterRunner();
        for (Runner runner : registerRunner) {
            try {
                runner.run(this);
            } catch (Throwable ex) {
                // ignore Exception
                log.error("run is failed", ex);
            }
        }
    }

    @Override
    protected void onRefreshComplete() throws Throwable {
        for (Runner runner : getCoreRunner().getRefreshCompleteRunner()) {
            try {
                runner.run(this);
            } catch (Throwable ex) {
                // ignore Exception
                log.error("runner: {} run is failed", runner, ex);
            }
        }
    }

    /**
     * 触发uno-core环境构建事件，子类实现。
     * 模板方法
     *
     * @deprecated 1.1.4版本之后删除
     */
    @Deprecated
    protected void onEnvBuild() {

    }

    @Override
    protected void onContextClose() throws Throwable {
        for (Runner runner : getCoreRunner().getCloseRunner()) {
            try {
                runner.run(this);
            } catch (Throwable ex) {
                // ignore Exception
                log.error("runner: {} run is failed", runner, ex);
            }
        }
    }

    @Override
    public RunTestAttributes getRunTestAttributes() {
        return new RunTestAttributes(getTestClass());
    }

    /**
     * 子类提供Spring环境类
     * 模板方法
     *
     * @return 环境类实例
     * @deprecated 1.1.4版本之后删除
     */
    @Deprecated
    public Environment supportEnv() {
        return null;
    }

    // ----------------- get/set -----------------

    public void setEnv(EnvironmentFacade env) {
        this.env = env;
    }

    /**
     * 获取当前测试的环境实例
     *
     * @return
     */
    public EnvironmentFacade getEnv() {
        return env;
    }

    /**
     * set CoreRunner
     *
     * @param coreRunner coreRunner
     */
    public void setCoreRunner(CoreRunner coreRunner) {
        this.coreRunner = coreRunner;
    }

    /**
     * get coreRunner
     *
     * @return coreRunner
     */
    public CoreRunner getCoreRunner() {
        return coreRunner;
    }

    /**
     * 设置Visitor
     *
     * @param visitors visitors
     */
    public void setVisitors(Set<Visitor> visitors) {
        this.visitors = visitors;
    }

    /**
     * 获取Visitor
     *
     * @return visitors
     */
    public Set<Visitor> getVisitors() {
        return this.visitors;
    }

    /**
     * 获取当前测试class对象
     *
     * @return testClass
     */
    public Class<?> getTestClass() {
        return testClass;
    }

    /**
     * 获取当前测试实例
     *
     * @return this
     */
    public Object getTestInstance() {
        return this;
    }

    // ----------------- annotation -----------------

    /**
     * 获取指定注解的{@link MergedAnnotation}。如果注解是在测试类上，那么取测试类上，如果不在则取{@link RunTest#components()}中给定的配置类
     *
     * @param annoType 指定的注解类型
     * @return MergedAnnotation实例 or null
     */
    public <T extends Annotation> MergedAnnotation<T> getMergedAnnotation(Class<T> annoType) {
        // 从当前测类是中寻找
        MergedAnnotations testClassAnnos = MergedAnnotations.from(getTestClass());
        if (testClassAnnos.isPresent(annoType)) {
            return testClassAnnos.get(annoType);
        }
        // 从components中寻找
        MergedAnnotation<RunTest> runAnno = testClassAnnos.get(RunTest.class);
        Class<?>[] components = runAnno.getClassArray("components");
        for (Class<?> component : components) {
            testClassAnnos = MergedAnnotations.from(component);
            if (testClassAnnos.isPresent(annoType)) {
                return testClassAnnos.get(annoType);
            }
        }
        return null;
    }

    /**
     * 获取指定注解。如果注解是在测试类上，那么取测试类上，如果不在则取{@link RunTest#components()}中给定的配置类
     *
     * @param annoType 指定的注解类型
     * @return Annotation or null
     */
    public Annotation getAnnotation(Class<? extends Annotation> annoType) {
        Annotation annotation = AnnotationUtils.findAnnotation(getTestClass(), annoType);
        if (annotation == null) {
            Set<Class<?>> components = getRunTestAttributes().getComponentsClasses();
            for (Class<?> component : components) {
                annotation = AnnotationUtils.findAnnotation(component, annoType);
                if (annotation != null) {
                    return annotation;
                }
            }
        }
        return annotation;
    }


    /**
     * 判断指定的注解是否存在，先判断是否在测试类上，在判断是否在{@link RunTest#components()}中给定的配置类上
     *
     * @param annoType 指定的注解类型
     * @return true 存在 false 不存在
     */
    public boolean isAnnotation(Class<? extends Annotation> annoType) {
        boolean candidateClass = AnnotationUtils.isCandidateClass(getTestClass(), annoType);
        if (!candidateClass) {
            Set<Class<?>> components = getRunTestAttributes().getComponentsClasses();
            return components.stream().anyMatch(c -> AnnotationUtils.isCandidateClass(c, annoType));
        }
        return true;
    }

    /**
     * 获取当前测试类上所有的注解
     *
     * @return AnnotationMetadata
     */
    public AnnotationMetadata getCoreTestAnnotations() {
        return AnnotationMetadata.introspect(getTestClass());
    }

    /**
     * 获取{@link RunTest#components()}中所有的注解数据
     *
     * @return Set AnnotationMetadata
     */
    public Set<AnnotationMetadata> getComponentAnnotations() {
        Set<AnnotationMetadata> annotationMetadatas = Sets.newHashSet();
        MergedAnnotation<RunTest> annotation = getMergedAnnotation(RunTest.class);
        Class<?>[] components = annotation.getClassArray("components");
        for (Class<?> component : components) {
            annotationMetadatas.add(AnnotationMetadata.introspect(component));
        }
        return annotationMetadatas;
    }

    /**
     * 获取所有的注解数据。包含测试类上存在的与{@link RunTest#components()}上存在的，如果
     * 测试类上的注解包含在{@link RunTest#components()}上，则取测试类上
     *
     * @return MergedAnnotations
     */
    public MergedAnnotations getAllAnnotations() {
        Set<MergedAnnotation<?>> annotations = Sets.newHashSet();
        AnnotationMetadata coreAnnotations = getCoreTestAnnotations();
        Iterator<MergedAnnotation<Annotation>> iterator = coreAnnotations.getAnnotations().iterator();
        // 添加测试类上存在的所有注解
        annotations.addAll(Lists.newArrayList(iterator));
        Set<AnnotationMetadata> componentAnnotations = getComponentAnnotations();
        annotations.addAll(
                componentAnnotations.stream()
                        .flatMap(annoMeta -> annoMeta.getAnnotations().stream())
                        // 判断测试类上是否包含component的注解
                        .filter(anno -> !coreAnnotations.isAnnotated(anno.getType().getName()))
                        .collect(Collectors.toSet())
        );
        return MergedAnnotations.from(
                annotations.stream()
                        .map(MergedAnnotation::synthesize)
                        .toArray(Annotation[]::new)
        );
    }
}
