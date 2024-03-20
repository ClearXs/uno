package cc.allio.uno.test.testcontainers;

import cc.allio.uno.core.StringPool;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.testcontainers.containers.GenericContainer;

/**
 * describe {@link org.testcontainers.containers.Container}
 *
 * @author j.x
 * @date 2024/3/20 00:40
 * @since 1.1.7
 */
@Data
@AllArgsConstructor
public final class Container {

    private final ContainerType containerType;
    private final GenericContainer<?> internal;

    /**
     * get container env by key
     *
     * @param key the env key
     * @return env info
     */
    public String getEnv(String key) {
        if (internal != null) {
            return internal.getEnvMap().get(key);
        }
        return StringPool.EMPTY;
    }

    /**
     * start internal container
     *
     * @see GenericContainer#start()
     */
    public void start() {
        if (internal != null) {
            internal.start();
        }
    }

    /**
     * shutdown internal container
     *
     * @see GenericContainer#stop()
     */
    public void shutdown() {
        if (internal != null) {
            internal.stop();
        }
    }
}
