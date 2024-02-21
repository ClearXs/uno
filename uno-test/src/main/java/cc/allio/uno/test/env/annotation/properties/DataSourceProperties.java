package cc.allio.uno.test.env.annotation.properties;

import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;

import java.lang.annotation.*;

/**
 * {@link org.springframework.boot.autoconfigure.jdbc.DataSourceProperties}的注解描述
 *
 * @author jiangwei
 * @date 2023/3/6 16:59
 * @since 1.1.4
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Properties("spring.datasource")
public @interface DataSourceProperties {

    /**
     * Name of the datasource. Default to "testdb" when using an embedded database.
     */
    String name() default "testdb";

    /**
     * Whether to generate a random datasource name.
     */
    boolean generateUniqueName() default true;

    /**
     * Fully qualified name of the JDBC driver. Auto-detected based on the URL by default.
     */
    String driverClassName();

    /**
     * JDBC URL of the database.
     */
    String url();

    /**
     * Login username of the database.
     */
    String username();

    /**
     * Login password of the database.
     */
    String password();

    /**
     * JNDI location of the datasource. Class, url, username and password are ignored when
     * setValue.
     */
    String jndiName() default "";

    /**
     * Platform to use in the DDL or DML scripts (such as schema-${platform}.sql or
     * data-${platform}.sql).
     */
    String platform() default "all";

    /**
     * Schema (DDL) script resource references.
     */
    String[] schema() default {};

    /**
     * Username of the database to execute DDL scripts (if different).
     */
    String schemaUsername() default "";

    /**
     * Password of the database to execute DDL scripts (if different).
     */
    String schemaPassword() default "";

    /**
     * Data (DML) script resource references.
     */
    String[] data() default {};

    /**
     * Username of the database to execute DML scripts (if different).
     */
    String dataUsername() default "";

    /**
     * Password of the database to execute DML scripts (if different).
     */
    String dataPassword() default "";

    /**
     * Whether to stop if an error occurs while initializing the database.
     */
    boolean continueOnError() default false;

    /**
     * Statement separator in SQL initialization scripts.
     */
    String separator() default ";";

    EmbeddedDatabaseConnection embeddedDatabaseConnection() default EmbeddedDatabaseConnection.NONE;

    String uniqueName() default "";
}
