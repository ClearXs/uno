package cc.allio.uno.test.env.annotation;

import cc.allio.uno.test.env.DynamicDataSourceEnvironment;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@AnnoConfigure
@Env({DynamicDataSourceEnvironment.class})
public @interface DynamicDataSourceEnv {
}
