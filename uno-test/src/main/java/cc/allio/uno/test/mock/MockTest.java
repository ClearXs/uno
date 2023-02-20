package cc.allio.uno.test.mock;

import java.lang.annotation.*;

/**
 * 模拟接口返回的数据，在test环境下有效
 *
 * @author jw
 * @date 2021/12/15 19:52
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface MockTest {

    /**
     * 模拟数据的Class对象
     *
     * @return Class对象
     */
    Class<? extends Mock<?>> value();
}
