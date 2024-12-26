package cc.allio.uno.test;

import cc.allio.uno.core.api.Self;
import cc.allio.uno.core.reflect.InstantiationFeature;
import cc.allio.uno.core.spi.ClassPathServiceLoader;
import cc.allio.uno.core.util.ClassUtils;
import cc.allio.uno.core.util.ObjectUtils;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.test.env.PropertiesVisitor;
import cc.allio.uno.test.env.Visitor;
import cc.allio.uno.test.listener.CoreTestListener;
import cc.allio.uno.test.listener.Listener;
import cc.allio.uno.test.listener.WebListener;
import cc.allio.uno.test.runner.*;
import cc.allio.uno.test.testcontainers.SetupContainer;
import cc.allio.uno.test.testcontainers.ShutdownContainer;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * {@link RunTest}的属性描述。支持能够在其上拓展增加能力
 *
 * @author j.x
 * @since 1.1.4
 */
@Slf4j
public class RunTestAttributes {

    // 配置文件名称
    @Getter
    private String profile = "uno";
    // 配置文件对应环境
    @Getter
    private String active = "test";
    // spring bean集合
    @Getter
    private final Set<Class<?>> componentsClasses = Sets.newHashSet();
    // spring auto configuration集合
    @Getter
    private final Set<Class<?>> autoConfigurationClasses = Sets.newHashSet();
    @Getter
    private final Set<String> inlineProperties = Sets.newHashSet();
    @Getter
    private RunTest.WebEnvironment webEnvironment = RunTest.WebEnvironment.NONE;
    private final Set<Class<? extends Runner>> runnerClasses = Sets.newHashSet();
    private final Set<Class<? extends Visitor>> visitorClasses = Sets.newHashSet();
    private final Set<Class<? extends Listener>> listenersClasses = Sets.newHashSet();
    private final List<Applicator> applicators = Lists.newArrayList(new InlinePropertiesApplicator(), new ComponentApplicator(), new ProfileApplicator());

    // 实例化
    @Getter
    private final Set<Runner> runners = new ClassesHashSet<>(runnerClasses);
    @Getter
    private final Set<Visitor> visitors = new ClassesHashSet<>(visitorClasses);
    @Getter
    private final Set<Listener> listeners = new ClassesHashSet<>(listenersClasses);
    private final RunnerCenter runnerCenter = new RunnerCenter();

    private static final String AUTO_CONFIGURATION_SUFFIX = "AutoConfiguration";

    public RunTestAttributes(Class<?> testClass) {
        RunTest runTest =
                MergedAnnotations.from(testClass, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY)
                        .get(RunTest.class)
                        .synthesize(MergedAnnotation::isPresent)
                        .orElse(null);
        if (runTest != null) {
            this.profile = runTest.profile();
            this.active = runTest.active();
            this.webEnvironment = runTest.webEnvironment();
            addComponents(runTest.components());
            addInlineProperties(runTest.properties());
        }
        initial(runTest);
    }

    private void initial(RunTest runTest) {
        ExtensionInitialization<Visitor> visitorInitialization
                = new ExtensionInitializationImpl<>(Visitor.class)
                .initialDefaultExtension(PropertiesVisitor.class)
                .initialExtensionBySPI();

        ExtensionInitialization<Runner> runnerInitialization
                = new ExtensionInitializationImpl<>(Runner.class)
                .initialDefaultExtension(InjectRunner.class, AnnoMetadataRunner.class, SetupContainer.class, ShutdownContainer.class)
                .initialExtensionBySPI();

        ExtensionInitialization<Listener> listenerInitialization
                = new ExtensionInitializationImpl<>(Listener.class)
                .initialDefaultExtension(CoreTestListener.class, PrintTimingListener.class, WebListener.class)
                .initialExtensionBySPI();

        if (runTest != null) {
            visitorInitialization.initialAnnoExtension(runTest::visitor);
            runnerInitialization.initialAnnoExtension(runTest::runner);
            listenerInitialization.initialAnnoExtension(runTest::listeners);
        }

        visitorInitialization.initial((classes, v) -> {
            this.visitorClasses.addAll(classes);
            this.visitors.addAll(v);
        });

        runnerInitialization.initial((classes, v) -> {
            this.runnerClasses.addAll(classes);
            this.runners.addAll(v);
        });

        listenerInitialization.initial((classes, v) -> {
            this.listenersClasses.addAll(classes);
            this.listeners.addAll(v);
        });
    }

    /**
     * add component class to {@link #componentsClasses}
     *
     * @param componentClasses the componentClasses
     */
    public void addComponents(Class<?>... componentClasses) {
        this.componentsClasses.addAll(Lists.newArrayList(componentClasses));
        addAutoConfigurationClasses(componentClasses);
    }

    /**
     * 添加至 auto configuration
     *
     * @param configurationClasses configurationClasses
     */
    public void addAutoConfigurationClasses(Class<?>... configurationClasses) {
        if (configurationClasses != null) {
            for (Class<?> configurationClass : configurationClasses) {
                if (checkIsAutoConfiguration(configurationClass)) {
                    autoConfigurationClasses.add(configurationClass);
                }
            }
        }
    }

    /**
     * 添加inlineProperties数据
     *
     * @param inlineProperties inlineProperties
     */
    public void addInlineProperties(String... inlineProperties) {
        this.inlineProperties.addAll(Lists.newArrayList(inlineProperties));
    }

    /**
     * 构建 CoreRunner
     *
     * @return CoreRunner
     */
    public CoreRunner getCoreRunner() {
        try {
            runnerCenter.register(runners);
            return runnerCenter.getRunner();
        } finally {
            runnerCenter.clear();
        }
    }

    /**
     * 基于构建的{@link RunTestAttributes}把数据应用于{@link CoreTest}环境中。
     * <ul>
     *     <li>{@link RunTest#profile()}</li>
     *     <li>{@link RunTest#active()}</li>
     *     <li>{@link RunTest#properties()}</li>
     *     <li>{@link RunTest#components()}</li>
     * </ul>
     *
     * @param coreTest coreTest
     */
    public void apply(BaseSpringTest coreTest) {
        applicators.forEach(applicator -> applicator.apply(coreTest, this));
    }

    /**
     * 检查是否是Auto Configuration
     * <ul>
     *     <li>判断该类注解上是否包含{@link org.springframework.context.annotation.Configuration}</li>
     *     <li>判断该类后缀是否以AutoConfiguration结尾</li>
     * </ul>
     *
     * @param maybe 可以是
     * @return true 是 false
     */
    private boolean checkIsAutoConfiguration(Class<?> maybe) {
        if (maybe == null) {
            return false;
        }
        boolean containsConfiguration = AnnotationUtils.isCandidateClass(maybe, Configuration.class);
        if (containsConfiguration) {
            // 判断名称
            String className = maybe.getName();
            return className.contains(AUTO_CONFIGURATION_SUFFIX);
        }
        return false;
    }

    /**
     * 连接{@link RunTest}中注释的属性，应用于CoreTest所构建的spring环境中
     */
    @FunctionalInterface
    interface Applicator {

        /**
         * 应用
         *
         * @param coreTest       coreTest
         * @param testAttributes testAttributes
         */
        void apply(BaseSpringTest coreTest, RunTestAttributes testAttributes);
    }

    /**
     * spring profile
     */
    static class ProfileApplicator implements Applicator {

        @Override
        public void apply(BaseSpringTest coreTest, RunTestAttributes testAttributes) {
            String profile = testAttributes.getProfile();
            String active = testAttributes.getActive();
            coreTest.addProperty("spring.config.name", profile);
            coreTest.addProperty("spring.profiles.active", active);
        }
    }

    /**
     * inline-properties应用
     */
    static class InlinePropertiesApplicator implements Applicator {

        @Override
        public void apply(BaseSpringTest coreTest, RunTestAttributes testAttributes) {
            Set<String> properties = testAttributes.getInlineProperties();
            coreTest.addInlineProperty(StringUtils.toStringArray(properties));
        }
    }

    /**
     * component应用
     * <ul>
     *     <li>注册至{@link #addComponents(Class[])}</li>
     *     <li>添加至{@link CoreTest#registerComponent(Class[])}</li>
     * </ul>
     */
    static class ComponentApplicator implements Applicator {

        @Override
        public void apply(BaseSpringTest coreTest, RunTestAttributes testAttributes) {
            TestComponentScanner scanner = new TestComponentScanner(coreTest.getContext());
            Set<Class<?>> registryComps = Sets.newHashSet(testAttributes.getComponentsClasses());
            List<Class<?>> loadComps = registryComps.stream()
                    .filter(com -> AnnotationUtils.isCandidateClass(com, TestComponentScan.class))
                    .flatMap(com -> {
                        TestComponentScan annotation = AnnotationUtils.findAnnotation(com, TestComponentScan.class);
                        if (annotation == null) {
                            return Stream.empty();
                        }
                        Class<?>[] value = annotation.value();
                        testAttributes.addComponents(value);
                        // 扫描包
                        String[] packages = annotation.basePackages();
                        if (ObjectUtils.isNotEmpty(packages)) {
                            Class<?>[] scansComps =
                                    scanner.doScan(annotation.basePackages())
                                            .stream()
                                            .map(bh -> {
                                                try {
                                                    return Class.forName(bh.getBeanDefinition().getBeanClassName());
                                                } catch (ClassNotFoundException ex) {
                                                    throw new RuntimeException(ex);
                                                }
                                            })
                                            .toArray(Class[]::new);
                            testAttributes.addComponents(scansComps);
                        }
                        return Stream.of(value);
                    })
                    .toList();
            registryComps.addAll(loadComps);
            coreTest.registerComponent(ClassUtils.toClassArray(registryComps));
        }
    }

    /**
     * 原始的{@link HashSet}添加元素只是比较于{@link Object#hashCode()}，但因为对象是由{@link Class}进行创建，这样导致每个对象的{@link Object#hashCode()}不一致。
     * 所以在创建的时候可能会出现：创建的实例数量不等于给定的class数量。为了修复这个问题，思路是在添加对象的时候判断实例数量是否等于classes数量
     *
     * @param <E>
     */
    private static class ClassesHashSet<E> extends HashSet<E> {

        private final Set<Class<? extends E>> classes;

        public ClassesHashSet(Set<Class<? extends E>> classes) {
            this.classes = classes;
        }

        @Override
        public boolean addAll(Collection<? extends E> c) {
            try {
                checkTheInstanceAndClassesQuantity(c);
                return super.addAll(c);
            } catch (IllegalArgumentException ex) {
                // 修正数据
                // 比较
                Iterator<? extends E> iterator = c.iterator();
                while (iterator.hasNext()) {
                    E ele = iterator.next();
                    // ele元素存在于集合中
                    boolean match = stream().anyMatch(o -> o.getClass().equals(ele.getClass()));
                    if (match) {
                        iterator.remove();
                    }
                }
                // 再次尝试比较
                Set<E> temp = Sets.newHashSet();
                temp.addAll(this);
                temp.addAll(c);
                if (temp.size() == classes.size()) {
                    return super.addAll(c);
                }
                // 否则抛出异常
                throw ex;
            }
        }

        /**
         * 检查实例的数量与需要进行实例化的class数量，当两者不一致时抛出异常
         *
         * @throws IllegalArgumentException 当实例化数量不一致时抛出
         */
        private void checkTheInstanceAndClassesQuantity(Collection<? extends E> c) {
            if (this.size() + c.size() != classes.size()) {
                throw new IllegalArgumentException(String.format("instance size is %s, but classes size is %s", this.size(), classes.size()));
            }
        }
    }

    interface ExtensionInitialization<E> extends Self<ExtensionInitialization<E>> {

        /**
         * initiation built in generic type extension
         *
         * @param defaultExtensionClasses the default extension class
         * @return self
         */
        ExtensionInitialization<E> initialDefaultExtension(Class<? extends E>... defaultExtensionClasses);

        /**
         * initiation extension by spi
         *
         * @return self
         */
        ExtensionInitialization<E> initialExtensionBySPI();

        /**
         * initiation by {@link RunTest} extension
         *
         * @param supplier supplier
         * @return self
         */
        ExtensionInitialization<E> initialAnnoExtension(Supplier<Class<? extends E>[]> supplier);

        /**
         * start initiation
         *
         * @param acceptor accept initial result
         */
        void initial(BiConsumer<Set<Class<? extends E>>, Collection<E>> acceptor);

        /**
         * 基于给定的class对象创建实例
         *
         * @param classes classes
         * @param <T>     类型
         * @return Collection
         */
        default <T> Collection<T> creation(Collection<Class<? extends T>> classes) {
            return ClassUtils.newInstanceList(Lists.newArrayList(classes), InstantiationFeature.sort(), InstantiationFeature.deduplicate());
        }
    }

    static class ExtensionInitializationImpl<E> implements ExtensionInitialization<E> {

        private final Class<E> extensionType;
        private final Set<Class<? extends E>> classes;

        public ExtensionInitializationImpl(Class<E> extensionType) {
            this.extensionType = extensionType;
            this.classes = Sets.newHashSet();
        }

        @Override
        @SafeVarargs
        public final ExtensionInitialization<E> initialDefaultExtension(Class<? extends E>... defaultExtensionClasses) {
            if (ObjectUtils.isNotEmpty(defaultExtensionClasses)) {
                List<Class<? extends E>> defaultExtensionList = Lists.newArrayList(defaultExtensionClasses);
                this.classes.addAll(defaultExtensionList);
            }
            return self();
        }

        @Override
        public ExtensionInitialization<E> initialExtensionBySPI() {
            // visitor
            ClassPathServiceLoader<E> load = ClassPathServiceLoader.load(extensionType);
            Lists.newArrayList(load)
                    .forEach(provider -> {
                        Class<? extends E> extensionClass = provider.type();
                        this.classes.add(extensionClass);
                    });
            return self();
        }

        @Override
        public ExtensionInitialization<E> initialAnnoExtension(Supplier<Class<? extends E>[]> supplier) {
            if (supplier != null) {
                Class<? extends E>[] extensionClass = supplier.get();
                if (extensionClass != null && extensionClass.length > 0) {
                    List<Class<? extends E>> extensionClassList = Lists.newArrayList(extensionClass);
                    this.classes.addAll(extensionClassList);
                }
            }
            return self();
        }

        @Override
        public void initial(BiConsumer<Set<Class<? extends E>>, Collection<E>> acceptor) {
            if (acceptor != null) {
                // create instance
                Collection<E> extension = creation(classes);
                acceptor.accept(classes, extension);
            }
        }

    }
}
