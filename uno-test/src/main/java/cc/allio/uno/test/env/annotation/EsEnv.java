package cc.allio.uno.test.env.annotation;

import cc.allio.uno.test.env.ElasticSearchEnvironment;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@AnnoConfigure
@Env({ElasticSearchEnvironment.class})
public @interface EsEnv {
}
