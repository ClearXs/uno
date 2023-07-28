package cc.allio.uno.test.env.annotation.properties;

import java.lang.annotation.*;

/**
 * {@link org.springframework.boot.autoconfigure.transaction.TransactionProperties}的注解描述
 *
 * @author jiangwei
 * @date 2023/3/6 16:59
 * @since 1.1.4
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Properties("spring.transaction")
public @interface TransactionProperties {


    /**
     * Whether to roll back on commit failures.
     */
    boolean rollbackOnCommitFailure() default false;
}
