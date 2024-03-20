package cc.allio.uno.test.testcontainers;

import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.core.util.ClassUtils;
import cc.allio.uno.test.CoreTest;
import cc.allio.uno.test.runner.RefreshCompleteRunner;
import jakarta.annotation.Priority;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Optional;

/**
 * setup test-container if exist {@link RunContainer}
 *
 * @author j.x
 * @date 2024/3/18 23:04
 * @since 1.1.7
 */
@Slf4j
@Priority(Integer.MIN_VALUE)
public class SetupContainer implements RefreshCompleteRunner {

    @Override
    public void onRefreshComplete(CoreTest coreTest) throws Throwable {
        Class<?> testClass = coreTest.getTestClass();
        RunContainer runTestContainer = AnnotationUtils.findAnnotation(testClass, RunContainer.class);
        Optional.ofNullable(runTestContainer)
                .map(RunContainer::value)
                .flatMap(containerType -> {
                    String className = containerType.getClassName();
                    if (log.isDebugEnabled()) {
                        log.debug("load test-container {} class", className);
                    }
                    try {
                        Class<GenericContainer<?>> containerClass = (Class<GenericContainer<?>>) Class.forName(className);
                        DockerImageName dockerImageName = containerType.getDockerImageName();
                        GenericContainer<?> c = ClassUtils.newInstance(containerClass, dockerImageName);
                        Container testContainer = new Container(containerType, c);
                        // start container
                        testContainer.start();
                        return Optional.of(testContainer);
                    } catch (Throwable ex) {
                        log.error("load class {} for test-container failed", className, ex);
                        throw Exceptions.unchecked(ex);
                    }
                })
                .ifPresent(coreTest::setContainer);
    }
}
