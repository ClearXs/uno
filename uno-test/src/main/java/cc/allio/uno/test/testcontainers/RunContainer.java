package cc.allio.uno.test.testcontainers;

import cc.allio.uno.test.UnoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.*;

/**
 * base on test-containers, built-in fast test-container startup
 *
 * @author j.x
 * @date 2024/3/18 22:54
 * @since 1.1.7
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@ExtendWith(UnoExtension.class)
public @interface RunContainer {

    /**
     * support test container
     *
     * @see ContainerType
     */
    ContainerType value();
}
