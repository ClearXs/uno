package cc.allio.uno.test.testcontainers;

import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.GenericContainer;

/**
 * mark test container
 *
 * @author j.x
 * @date 2024/3/21 00:28
 * @since 1.1.7
 */
@Slf4j
public class TestContainer extends GenericContainer<TestContainer> {

    @Override
    public void start() {
        log.debug("start test container... then nothing todo.");
    }

    @Override
    public void stop() {
        log.debug("stop test container...");
    }
}
