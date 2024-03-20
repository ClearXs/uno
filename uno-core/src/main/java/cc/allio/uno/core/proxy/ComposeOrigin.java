package cc.allio.uno.core.proxy;

import java.lang.annotation.*;

/**
 * 使用于代理对象。标识形参上，表示当前代理对象使用组合类型Compose对象的方法。
 * <blockquote>
 * <pre>
 *     class Test {
 *         void getValue(@ComposeOrigin Object obj);
 *     }
 *     class ComposeTest extends Test {
 *     	   Test[] tests;
 *         ComposeTes(Test... tests) {
 *             this.tests = tests;
 *         }
 *         void getValue(@ComposeOrigin Object obj) {
 *             ...
 *         }
 *     }
 * </pre>
 * </blockquote>
 *
 * @author j.x
 * @date 2021/12/28 12:27
 * @see ComposeSharable
 * @see ComposableInvocationInterceptor
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface ComposeOrigin {
}
