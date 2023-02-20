package cc.allio.uno.core.proxy;

import java.lang.annotation.*;

/**
 * 使用于代理对象。标记于方法上，表示当前方法可以使用组合类型的方法。<br/>
 * 比如说：Test的实例将使用ComposeTest#get()方法
 * <blockquote>
 * <pre>
 *     class Test {
 *         void get();
 *     }
 *     class ComposeTest extends Test {
 *     	   Test[] tests;
 *         ComposeTest(Test... tests) {
 *             this.tests = tests;
 *         }
 *         void get();
 *     }
 * </pre>
 * </blockquote>
 *
 * @author jiangwei
 * @date 2021/12/27 23:57
 * @see ComposableInvocationInterceptor
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ComposeSharable {
}
