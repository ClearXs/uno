package cc.allio.uno.test.testcontainers;

import lombok.extern.slf4j.Slf4j;

/**
 * log for {@link Container} information
 *
 * @author j.x
 * @date 2024/4/16 14:37
 * @since 1.1.8
 */
@Slf4j
public class LogPrelude implements Prelude {

    @Override
    public void onPrepare(Container container) {
        if (log.isDebugEnabled()) {
            log.debug("container is [{}]", container.getContainerType());
        }
    }

    @Override
    public boolean match(ContainerType containerType) {
        return true;
    }
}
