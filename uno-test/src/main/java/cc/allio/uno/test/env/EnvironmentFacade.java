package cc.allio.uno.test.env;

import cc.allio.uno.test.CoreTest;
import com.google.common.collect.Sets;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 外观类
 *
 * @author jiangwei
 * @date 2022/2/14 14:08
 * @since 1.0
 */
public class EnvironmentFacade implements Environment {

    private final Collection<Environment> environments;

    public EnvironmentFacade() {
        this.environments = Sets.newHashSet();
    }

    public EnvironmentFacade(Environment... environments) {
        List<Environment> environmentList = Arrays.asList(environments);
        AnnotationAwareOrderComparator.sort(environmentList);
        this.environments = environmentList;
    }

    @Override
    public void support(CoreTest coreTest) throws Throwable {
        environments.forEach(environment -> {
            try {
                environment.support(coreTest);
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        });
    }

    @Override
    public Class<? extends Annotation>[] getPropertiesAnnotation() {
        return null;
    }

    /**
     * 连接环境
     *
     * @param environment
     */
    public EnvironmentFacade concat(Environment environment) {
        this.environments.add(environment);
        return this;
    }

    public EnvironmentFacade concat(Collection<Environment> environments) {
        this.environments.addAll(environments);
        return this;
    }

    /**
     * 获取当前{@link Environment}数量，如果某个实体是外观类，那将获取该外观类的的大小
     *
     * @return 保皇
     */
    public int size() {
        return environments.stream()
                .reduce(0, (c1, c2) -> {
                    if (c2 instanceof EnvironmentFacade) {
                        return c1 + ((EnvironmentFacade) c2).size();
                    }
                    return c1 + 1;
                }, Integer::sum);
    }

    /**
     * 获取当前{@link Environment}实例集合，如果某个实体是外观类，那将获取该外观类的的{@link Environment}
     *
     * @return 集合
     */
    public Collection<Environment> getEnvironments() {
        return environments.stream()
                .flatMap(env -> {
                    if (env instanceof EnvironmentFacade) {
                        return ((EnvironmentFacade) env).getEnvironments().stream();
                    }
                    return Stream.of(env);
                })
                .collect(Collectors.toList());
    }
}
