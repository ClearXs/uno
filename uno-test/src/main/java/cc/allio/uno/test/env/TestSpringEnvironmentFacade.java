package cc.allio.uno.test.env;

import cc.allio.uno.test.BaseCoreTest;
import com.google.common.collect.Sets;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

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
public class TestSpringEnvironmentFacade implements TestSpringEnvironment {

    private final Collection<TestSpringEnvironment> environments;

    public TestSpringEnvironmentFacade() {
        this.environments = Sets.newHashSet();
    }

    public TestSpringEnvironmentFacade(TestSpringEnvironment... environments) {
        List<TestSpringEnvironment> environmentList = Arrays.asList(environments);
        AnnotationAwareOrderComparator.sort(environmentList);
        this.environments = environmentList;
    }

    @Override
    public void support(BaseCoreTest test) {
        environments.forEach(environment -> environment.support(test));
    }

    /**
     * 连接环境
     *
     * @param environment
     */
    public TestSpringEnvironmentFacade concat(TestSpringEnvironment environment) {
        this.environments.add(environment);
        return this;
    }

    public TestSpringEnvironmentFacade concat(Collection<TestSpringEnvironment> environments) {
        this.environments.addAll(environments);
        return this;
    }

    /**
     * 获取当前{@link TestEnvironment}数量，如果某个实体是外观类，那将获取该外观类的的大小
     *
     * @return 保皇
     */
    public int size() {
        return environments.stream()
                .reduce(0, (c1, c2) -> {
                    if (c2 instanceof TestSpringEnvironmentFacade) {
                        return c1 + ((TestSpringEnvironmentFacade) c2).size();
                    }
                    return c1 + 1;
                }, Integer::sum);
    }

    /**
     * 获取当前{@link TestEnvironment}实例集合，如果某个实体是外观类，那将获取该外观类的的{@link TestEnvironment}
     *
     * @return 集合
     */
    public Collection<TestSpringEnvironment> getEnvironments() {
        return environments.stream()
                .flatMap(env -> {
                    if (env instanceof TestSpringEnvironmentFacade) {
                        return ((TestSpringEnvironmentFacade) env).getEnvironments().stream();
                    }
                    return Stream.of(env);
                })
                .collect(Collectors.toList());
    }
}
