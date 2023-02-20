package cc.allio.uno.test;

import lombok.NonNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.boot.context.properties.ConfigurationBeanFactoryMetadata;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Spring测试，继承于该测试的类，不应该在在加上{@link BeforeEach}与{@link AfterEach}
 *
 * @author jw
 * @date 2021/12/15 23:22
 */
public abstract class BaseSpringTest extends BaseTestCase {

    /**
     * 注解spring上下文对象
     */
    private AnnotationConfigApplicationContext context;

    /**
     * <ol>
     *     <li>{{@link #onInitSpringEnv()}}初始化当前Spring环境</li>
     *     <li>注册默认的Spring-Component组件</li>
     *     <li>加载类路径下的Spring配置文件到当前容器中</li>
     *     <li>调用{@link AbstractApplicationContext#refresh()}加载当前放入上下文的组件</li>
     *     <li>调用{{@link #onRefreshComplete()}}方法</li>
     * </ol>
     *
     * @throws Throwable 初始化过程中如果有异常发生则抛出该异常
     */
    @Override
    protected void onInit() throws Throwable {
        context = new AnnotationConfigApplicationContext();
        // 从当前项目路径上下文添加配置文件
        // 添加配置文件属性
        addConfigurationFile();
        new ConfigFileApplicationListener() {
            public void apply() {
                addPropertySources(context.getEnvironment(), context);
                addPostProcessors(context);
            }
        }.apply();
        onInitSpringEnv();
        registerDefaultComponent();
        context.refresh();
        onRefreshComplete();
    }

    @Override
    protected void onDown() throws Throwable {
        onContextClose();
        if (context != null) {
            context.close();
        }
    }

    /**
     * 向spring环境中添加配置
     *
     * @param key   配置的key
     * @param value 配置value
     */
    public void addProperty(String key, String value) {
        if (!StringUtils.isEmpty(value)) {
            TestPropertyValues
                    .of(key.concat("=").concat(value))
                    .applyTo(context);
        }
    }

    /**
     * 向Spring环境中添加配置
     *
     * @param properties 配置文件
     */
    public void addProperty(@NonNull Properties properties) {
    }

    /**
     * 从Map中提取Key，value向Spring环境中添加配置
     *
     * @param prefix 配置前缀，如spring.server
     * @param map    map实例对象
     */
    public void addPropertyWithMap(String prefix, Map<String, String> map) {
        if (CollectionUtils.isEmpty(map)) {
            return;
        }
        Set<Map.Entry<String, String>> entries = map.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            TestPropertyValues
                    .of(".".concat(prefix).concat(".").concat(entry.getKey()).concat("=").concat(entry.getValue()))
                    .applyTo(context.getEnvironment(), TestPropertyValues.Type.MAP);
        }
    }

    /**
     * 把List的数据向Spring环境中添加配置
     *
     * @param prefix 配置前缀，如spring.server
     * @param list   list实例对象
     */
    public void addPropertyWithList(String prefix, List<String> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            addProperty(prefix.concat("[").concat(String.valueOf(i)).concat("]"), String.valueOf(list.get(i)));
        }
    }

    public void addPropertySource(PropertySource<?> propertySource) {
        ConfigurableEnvironment environment = getContext().getEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();
        propertySources.addLast(propertySource);
    }

    public void addPropertySources(List<PropertySource<?>> propertySources) {
        ConfigurableEnvironment environment = getContext().getEnvironment();
        MutablePropertySources origin = environment.getPropertySources();
        for (PropertySource<?> propertySource : propertySources) {
            origin.addLast(propertySource);
        }
    }

    /**
     * 根据名称获取配置
     *
     * @param name 配置名称
     * @return 获取的String类型的配置
     * @see org.springframework.core.env.Environment#getProperty(String)
     */
    public String getProperty(String name) {
        return context.getEnvironment().getProperty(name);
    }


    /**
     * 根据名称获取期望的配置，
     *
     * @param name         配置名称
     * @param defaultValue 默认值
     * @return 获取的String类型的配置
     */
    public String getProperty(String name, String defaultValue) {
        return getProperty(name, String.class, defaultValue);
    }

    /**
     * 根据名称获取期望的配置，如果没有则返回默认值
     *
     * @param name       配置名称
     * @param expectType 期望类型的Class对象
     * @param <T>        期望的类型
     * @return 获取的期望类型的配置
     * @see org.springframework.core.env.Environment#getProperty(String, Class)
     */
    public <T> T getProperty(String name, Class<T> expectType) {
        return getProperty(name, expectType, null);
    }

    /**
     * 根据名称获取期望的配置，如果没有则返回默认值
     *
     * @param name         配置名称
     * @param expectType   期望类型的Class对象
     * @param defaultValue 默认值
     * @param <T>          期望的类型
     * @return 获取的期望类型的配置
     */
    public <T> T getProperty(String name, Class<T> expectType, T defaultValue) {
        return context.getEnvironment().getProperty(name, expectType, defaultValue);
    }

    /**
     * 向Spring BeanFactory注册
     *
     * @param bean 注册bean对象
     */
    public <T> void register(Class<T> beanType, T bean) {
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getBeanFactory();
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(beanType, () -> bean);
        beanFactory.registerBeanDefinition(beanType.getName(), beanDefinitionBuilder.getRawBeanDefinition());
    }

    /**
     * spring环境中注册默认的bean
     */
    public void registerDefaultComponent() {
        registerComponent(ConfigurationBeanFactoryMetadata.class);
    }

    /**
     * spring环境添加默认的配置属性
     */
    private void addConfigurationFile() {
        // 添加默认加载uno-test配置文件
        Set<RunTest> mergedAnnotations = AnnotatedElementUtils.findAllMergedAnnotations(this.getClass(), RunTest.class);
        if (CollectionUtils.isEmpty(mergedAnnotations)) {
            addProperty("spring.config.name", "uno");
            addProperty("spring.profiles.active", "test");
        } else {
            // name
            String configNames = String.join(",", mergedAnnotations.stream()
                    .filter(str -> !StringUtils.isEmpty(str))
                    .flatMap(runTest -> Arrays.stream(StringUtils.commaDelimitedListToStringArray(runTest.profile())))
                    .collect(Collectors.toSet()));
            addProperty("spring.config.name", configNames);
            String activeNames = String.join(",", mergedAnnotations.stream()
                    .filter(str -> !StringUtils.isEmpty(str))
                    .flatMap(runTest -> Arrays.stream(StringUtils.commaDelimitedListToStringArray(runTest.active())))
                    .collect(Collectors.toSet()));
            addProperty("spring.profiles.active", activeNames);
        }
    }

    /**
     * 向Spring BeanFactory注册被{@link Component}标识的类
     *
     * @param components 组件Class数组对象
     */
    public void registerComponent(Class<?>... components) {
        context.register(components);
    }

    public <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }

    /**
     * 获取Spring上下文实例对象
     *
     * @return
     */
    public AnnotationConfigApplicationContext getContext() {
        return context;
    }

    /**
     * 初始化Spring环境，如注册某个Bean，添加某些配置文件</br>
     *
     * @throws Throwable 初始化时抛出
     */
    protected abstract void onInitSpringEnv() throws Throwable;

    /**
     * <b>单元测试初始化</b>上下文完成刷新后进行调用.
     *
     * @throws Throwable 回调产生异常时抛出
     */
    protected void onRefreshComplete() throws Throwable {

    }

    /**
     * <b>单元测试销毁</b>当上下文对象要被关闭时触发，
     *
     * @throws Throwable 回调产生异常时抛出
     */
    protected void onContextClose() throws Throwable {

    }
}
