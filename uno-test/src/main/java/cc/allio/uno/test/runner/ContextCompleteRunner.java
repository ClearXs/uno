package cc.allio.uno.test.runner;

import cc.allio.uno.test.CoreTest;
import cc.allio.uno.test.RunBefore;
import org.springframework.util.ReflectionUtils;

import java.util.Arrays;

/**
 * <b>上下文刷新完成后回调</b>
 * <pre>
 *     调用该测试类中被{@link RunBefore}标识的方法
 * </pre>
 *
 * @author jiangwei
 * @date 2022/10/29 12:44
 * @since 1.1.0
 */
@Deprecated
public class ContextCompleteRunner implements RefreshCompleteRunner {

    @Override
    public void onRefreshComplete(CoreTest coreTest) throws Throwable {
        Class<?> testClass = coreTest.getTestClass();
        Arrays.stream(ReflectionUtils.getDeclaredMethods(testClass))
                .filter(method -> method.isAnnotationPresent(RunBefore.class))
                .forEach(method -> {
                    ReflectionUtils.makeAccessible(method);
                    ReflectionUtils.invokeMethod(method, this);
                });
    }
}