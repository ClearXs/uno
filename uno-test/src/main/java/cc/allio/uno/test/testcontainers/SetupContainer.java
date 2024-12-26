package cc.allio.uno.test.testcontainers;

import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.core.spi.ClassPathServiceLoader;
import cc.allio.uno.core.util.ClassUtils;
import cc.allio.uno.test.CoreTest;
import cc.allio.uno.test.runner.RefreshCompleteRunner;
import com.google.common.collect.Lists;
import jakarta.annotation.Priority;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;

/**
 * setup test-container if exist {@link RunContainer}
 *
 * @author j.x
 * @since 1.1.7
 */
@Slf4j
@Priority(Integer.MIN_VALUE)
public class SetupContainer implements RefreshCompleteRunner {

    private final List<Prelude> preludes;

    public SetupContainer() {
        this.preludes = Lists.newArrayList();
        // load prelude instance by spi
        var preludeOnSPI = Lists.newArrayList(ClassPathServiceLoader.load(Prelude.class))
                .stream()
                .map(ServiceLoader.Provider::get)
                .toList();
        this.preludes.addAll(preludeOnSPI);
    }

    @Override
    public void onRefreshComplete(CoreTest coreTest) throws Throwable {
        Class<?> testClass = coreTest.getTestClass();
        RunContainer runTestContainer = AnnotationUtils.findAnnotation(testClass, RunContainer.class);
        Optional.ofNullable(runTestContainer)
                .map(RunContainer::value)
                .flatMap(containerType -> {
                    String className = containerType.getTestContainerClassName();
                    if (log.isDebugEnabled()) {
                        log.debug("load test-container {} class", className);
                    }

                    Class<GenericContainer<?>> containerClass;
                    try {
                        containerClass = (Class<GenericContainer<?>>) Class.forName(className);
                    } catch (Throwable ex) {
                        log.error("load class {} for test-container failed", className, ex);
                        throw Exceptions.unchecked(ex);
                    }
                    DockerImageName dockerImageName = containerType.getDockerImageName();
                    GenericContainer<?> c = ClassUtils.newInstance(containerClass, dockerImageName);
                    // load RunContainer prelude
                    Class<? extends Prelude> preludeClass = runTestContainer.prelude();
                    if (preludeClass != null) {
                        Prelude prelude = ClassUtils.newInstance(preludeClass);
                        preludes.add(prelude);
                    }
                    Container testContainer = new Container(containerType, c, preludes);
                    try {
                        // on prepare container
                        testContainer.prelude();
                    } catch (Throwable ex) {
                        log.error("Field to prelude Container [{}]. ", containerType, ex);
                    }
                    // start container, if error then throwing
                    testContainer.start();
                    return Optional.of(testContainer);
                })
                .ifPresent(coreTest::setContainer);
    }
}
