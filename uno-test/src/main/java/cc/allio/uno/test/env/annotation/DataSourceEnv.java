package cc.allio.uno.test.env.annotation;

import cc.allio.uno.test.env.DataSourceEnvironment;
import cc.allio.uno.test.env.TransactionEnvironment;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@AnnoConfigure
@Env({DataSourceEnvironment.class, TransactionEnvironment.class})
public @interface DataSourceEnv {
}
