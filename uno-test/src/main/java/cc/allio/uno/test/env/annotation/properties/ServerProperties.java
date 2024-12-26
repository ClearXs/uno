package cc.allio.uno.test.env.annotation.properties;

import org.springframework.boot.web.server.Shutdown;

import java.lang.annotation.*;

/**
 * {@link org.springframework.boot.autoconfigure.web.ServerProperties}的注解描述
 *
 * @author j.x
 * @since 1.1.4
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Properties("server")
public @interface ServerProperties {

    /**
     * Server HTTP port.
     */
    int port() default 8080;

    /**
     * Network address to which the server should bind.
     */
    String address() default "localhost";

    ErrorProperties errorProperties();

    /**
     * Value to use for the Server response header (if empty, no header is sent).
     */
    String serverHeader() default "";

    /**
     * Type of shutdown that the server will support.
     */
    Shutdown shutdown() default Shutdown.IMMEDIATE;

    /**
     * {@link org.springframework.boot.autoconfigure.web.ErrorProperties}的注解描述
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Properties
    @interface ErrorProperties {

        /**
         * Path of the error controller.
         */
        String path() default "/error";

        /**
         * Include the "exception" attribute.
         */
        boolean includeException() default false;

        /**
         * When to include the "trace" attribute.
         */
        org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeStacktrace includeStacktrace() default org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeStacktrace.NEVER;

        /**
         * When to include "message" attribute.
         */
        org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeAttribute includeMessage() default org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeAttribute.NEVER;

        /**
         * When to include "errors" attribute.
         */
        org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeAttribute includeBindingErrors() default org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeAttribute.NEVER;

        Whitelabel whitelabel();

        /**
         * {@link org.springframework.boot.autoconfigure.web.ErrorProperties.Whitelabel}注解描述
         */
        @interface Whitelabel {

            /**
             * Whether to enable the default error page displayed in browsers in case of a server error.
             */
            boolean enabled() default true;
        }
    }
}
