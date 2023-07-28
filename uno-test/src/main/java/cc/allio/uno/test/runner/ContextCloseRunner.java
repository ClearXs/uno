package cc.allio.uno.test.runner;

import cc.allio.uno.test.RunAfter;
import cc.allio.uno.test.CoreTest;
import org.springframework.util.ReflectionUtils;

import java.util.Arrays;

/**
 * 当Spring上下文关闭时调用
 *
 * @author jiangwei
 * @date 2022/11/1 18:20
 * @since 1.1.0
 */
@Deprecated
public class ContextCloseRunner implements CloseRunner {

    @Override
    public void onClose(CoreTest coreTest) throws Throwable {
        Class<?> testClass = coreTest.getTestClass();
        Arrays.stream(ReflectionUtils.getDeclaredMethods(testClass))
                .filter(method -> method.isAnnotationPresent(RunAfter.class))
                .forEach(method -> {
                    ReflectionUtils.makeAccessible(method);
                    ReflectionUtils.invokeMethod(method, this);
                });
    }
}
