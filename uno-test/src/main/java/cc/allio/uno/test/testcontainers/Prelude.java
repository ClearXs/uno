package cc.allio.uno.test.testcontainers;

/**
 * prepare action {@link Container} start up before
 * <p>sub-class should be through SPI mechanism. </p>
 *
 * @author j.x
 * @date 2024/4/16 13:58
 * @since 1.1.8
 */
public interface Prelude {

    /**
     * on prepare in {@link Container#start()} before
     *
     * @param container the container instance
     */
    void onPrepare(Container container);

    /**
     * match specifies {@link ContainerType}. if match then invoke {@link #onPrepare(Container)}
     *
     * @param containerType the container type
     * @return true if match
     */
    boolean match(ContainerType containerType);
}
