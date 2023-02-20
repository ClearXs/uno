package cc.allio.uno.test.runner;

import cc.allio.uno.test.BaseCoreTest;
import cc.allio.uno.test.RunAfter;
import com.google.auto.service.AutoService;
import org.springframework.util.ReflectionUtils;

import java.util.Arrays;

/**
 * 当Spring上下文关闭时调用
 *
 * @author jiangwei
 * @date 2022/11/1 18:20
 * @since 1.1.0
 */
@AutoService(Runner.class)
public class ContextCloseRunner implements CloseRunner {

    @Override
    public void run(BaseCoreTest coreTest) throws Throwable {
        Arrays.stream(ReflectionUtils.getDeclaredMethods(coreTest.getClass()))
                .filter(method -> method.isAnnotationPresent(RunAfter.class))
                .forEach(method -> {
                    ReflectionUtils.makeAccessible(method);
                    ReflectionUtils.invokeMethod(method, this);
                });
    }
}
