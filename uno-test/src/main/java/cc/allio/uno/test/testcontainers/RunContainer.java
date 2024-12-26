package cc.allio.uno.test.testcontainers;

import cc.allio.uno.test.UnoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.*;

/**
 * base on test-containers, built-in fast test-container startup
 *
 * @author j.x
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

    /**
     * through give the class {@link Prelude}, build and trigger {@link Prelude#onPrepare(Container)}
     *
     * @return the class of {@link Prelude}
     */
    Class<? extends Prelude> prelude() default LogPrelude.class;
}
