package cc.allio.uno.test.feign;

import cc.allio.uno.test.BaseCoreTest;
import cc.allio.uno.test.runner.RefreshCompleteRunner;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.*;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import reactivefeign.spring.config.ReactiveFeignClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 注解{@link RunFeignTest}执行器
 *
 * @author jiangwei
 * @date 2022/10/28 17:31
 * @since 1.1.0
 */
public class FeignRunner implements RefreshCompleteRunner {

    @Override
    public void run(BaseCoreTest coreTest) throws Throwable {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false, coreTest.getContext().getEnvironment());
        scanner.setResourceLoader(coreTest.getContext());
        scanner.addIncludeFilter(new AnnotationTypeFilter(FeignClient.class));
        scanner.addIncludeFilter(new AnnotationTypeFilter(ReactiveFeignClient.class));

        LinkedHashSet<BeanDefinition> candidateComponents = new LinkedHashSet<>();

        Set<RunFeignTest> annotationFeign = AnnotatedElementUtils.findAllMergedAnnotations(coreTest.getClass(), RunFeignTest.class);
        Set<String> basePackages = annotationFeign.stream()
                .flatMap(feign -> loadCandidateComponents(feign).stream())
                .collect(Collectors.toSet());
        // 添加给定包下的组件
        for (String basePackage : basePackages) {
            candidateComponents.addAll(scanner.findCandidateComponents(basePackage));
        }

        // 添加指定feign class对象
        Set<Class<?>> clients = annotationFeign.stream()
                .filter(feign -> !ObjectUtils.isEmpty(feign.clients()))
                .flatMap(feign -> Sets.newHashSet(feign.clients()).stream())
                .collect(Collectors.toSet());
        for (Class<?> client : clients) {
            candidateComponents.add(new AnnotatedGenericBeanDefinition(client));
        }

        // 扫描后的client接口添加至Spring容器中
        FeignClientBuilder feignClientBuilder = new FeignClientBuilder(coreTest.getContext());

        ReactiveFeignClientsRegistrar registrar = new ReactiveFeignClientsRegistrar(coreTest.getContext().getEnvironment());

        for (BeanDefinition candidateComponent : candidateComponents) {
            Class typeComponent = ClassUtils.resolveClassName(candidateComponent.getBeanClassName(), null);
            AnnotationMetadata annotationMetadata = AnnotationMetadata.introspect(typeComponent);
            if (annotationMetadata.hasAnnotation(FeignClient.class.getName())) {
                if (candidateComponent instanceof AnnotatedBeanDefinition) {
                    Map<String, Object> attributes = annotationMetadata
                            .getAnnotationAttributes(FeignClient.class.getCanonicalName());
                    String className = getClientName(attributes);
                    String contextId = getContextId(coreTest.getContext().getBeanFactory(), coreTest.getContext().getEnvironment(), attributes);
                    FeignClientBuilder.Builder<?> builder = feignClientBuilder.forType(typeComponent, className);
                    String qualifier = getQualifier(attributes);
                    if (ObjectUtils.isEmpty(qualifier)) {
                        qualifier = contextId + "FeignClient";
                    }
                    BeanDefinitionBuilder clientBeanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(typeComponent, builder::build);
                    clientBeanDefinitionBuilder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
                    clientBeanDefinitionBuilder.setLazyInit(true);
                    AbstractBeanDefinition clientBeanDefinition = clientBeanDefinitionBuilder.getBeanDefinition();
                    BeanDefinitionHolder holder = new BeanDefinitionHolder(clientBeanDefinition, typeComponent.getName(), new String[]{qualifier});
                    BeanDefinitionReaderUtils.registerBeanDefinition(holder, coreTest.getContext());

                }
            }
            if (annotationMetadata.hasAnnotation(ReactiveFeignClient.class.getName())) {
                Map<String, Object> attributes = annotationMetadata
                        .getAnnotationAttributes(
                                ReactiveFeignClient.class.getCanonicalName());
                registrar.registerReactiveFeignClient(coreTest.getContext(), annotationMetadata, attributes);
            }
        }
    }

    /**
     * 加载被注解的组件
     *
     * @param runFeignTest 注解实例
     * @return
     */
    public Set<String> loadCandidateComponents(RunFeignTest runFeignTest) {
        Set<String> basePackages = Sets.newHashSet();
        basePackages.addAll(Arrays.asList(runFeignTest.basePackages()));

        Class<?>[] classes = runFeignTest.basePackagesClasses();
        for (Class<?> packClass : classes) {
            basePackages.add(ClassUtils.getPackageName(packClass));
        }
        return basePackages;
    }

    private String getClientName(Map<String, Object> client) {
        if (client == null) {
            return null;
        }
        String value = (String) client.get("contextId");
        if (!StringUtils.hasText(value)) {
            value = (String) client.get("value");
        }
        if (!StringUtils.hasText(value)) {
            value = (String) client.get("name");
        }
        if (!StringUtils.hasText(value)) {
            value = (String) client.get("serviceId");
        }
        if (StringUtils.hasText(value)) {
            return value;
        }

        throw new IllegalStateException(
                "Either 'name' or 'value' must be provided in @" + FeignClient.class.getSimpleName());
    }

    String getName(ApplicationContext applicationContext, Map<String, Object> attributes) {
        return getName(null, applicationContext.getEnvironment(), attributes);
    }

    String getName(ConfigurableBeanFactory beanFactory, Environment environment, Map<String, Object> attributes) {
        String name = (String) attributes.get("serviceId");
        if (!StringUtils.hasText(name)) {
            name = (String) attributes.get("name");
        }
        if (!StringUtils.hasText(name)) {
            name = (String) attributes.get("value");
        }
        name = resolve(beanFactory, environment, name);
        return getName(name);
    }

    private String getContextId(ConfigurableBeanFactory beanFactory, Environment environment, Map<String, Object> attributes) {
        String contextId = (String) attributes.get("contextId");
        if (!StringUtils.hasText(contextId)) {
            return getName(beanFactory, environment, attributes);
        }

        contextId = resolve(beanFactory, environment, contextId);
        return getName(contextId);
    }

    static String getName(String name) {
        if (!StringUtils.hasText(name)) {
            return "";
        }

        String host = null;
        try {
            String url;
            if (!name.startsWith("http://") && !name.startsWith("https://")) {
                url = "http://" + name;
            } else {
                url = name;
            }
            host = new URI(url).getHost();

        } catch (URISyntaxException e) {
        }
        Assert.state(host != null, "Service id not legal hostname (" + name + ")");
        return name;
    }

    private String resolve(ConfigurableBeanFactory beanFactory, Environment environment, String value) {
        if (StringUtils.hasText(value)) {
            if (beanFactory == null) {
                return environment.resolvePlaceholders(value);
            }
            BeanExpressionResolver resolver = beanFactory.getBeanExpressionResolver();
            String resolved = beanFactory.resolveEmbeddedValue(value);
            if (resolver == null) {
                return resolved;
            }
            Object evaluateValue = resolver.evaluate(resolved, new BeanExpressionContext(beanFactory, null));
            if (evaluateValue != null) {
                return String.valueOf(evaluateValue);
            }
            return null;
        }
        return value;
    }

    private String getQualifier(Map<String, Object> client) {
        if (client == null) {
            return null;
        }
        String qualifier = (String) client.get("qualifier");
        if (StringUtils.hasText(qualifier)) {
            return qualifier;
        }
        return null;
    }

    @Override
    public boolean shared() {
        return false;
    }
}
