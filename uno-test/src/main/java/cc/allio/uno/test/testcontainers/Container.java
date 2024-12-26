package cc.allio.uno.test.testcontainers;

import cc.allio.uno.core.StringPool;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.testcontainers.containers.GenericContainer;

import java.util.List;

/**
 * describe {@link org.testcontainers.containers.Container}
 *
 * @author j.x
 * @since 1.1.7
 */
@Data
@AllArgsConstructor
public final class Container {

    private final ContainerType containerType;
    private final GenericContainer<?> internal;
    private final List<Prelude> containerPrelude;

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
     * prepare container
     */
    public void prelude() {
        containerPrelude.stream()
                .filter(prelude -> prelude.match(containerType))
                .forEach(p -> p.onPrepare(this));
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
