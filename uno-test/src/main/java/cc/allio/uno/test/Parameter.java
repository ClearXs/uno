package cc.allio.uno.test;

import java.lang.annotation.*;

/**
 * 注解于测试方法的参数上，注释于该参数的类型将会从当前测试的上下文中寻找实例对象。
 * <p>Example：
 * </p>
 * <pre>
 * <code>
 * &copy;RunTest
 * public class Test  {
 *  &copy;Test
 *  void test(&copy;Parameter Test test) {
 *      ...
 *  }
 * }
 * </code>
 * </pre>
 *
 * @author jiangwei
 * @date 2023/3/10 12:40
 * @since 1.1.4
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Parameter {
}
