package cc.allio.uno.test.runner;

import cc.allio.uno.test.feign.FeignRunner;
import com.google.common.collect.Lists;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * {@link Runner}列表中心
 *
 * @author jiangwei
 * @date 2022/10/28 17:33
 * @since 1.1.0
 */
public class RunnerCenter {

    private static volatile RunnerCenter instance;

    /**
     * 可以被共享的Runner实例
     */
    public final RunnerList sharedRunner = new RunnerList();

    public RunnerCenter() {
        sharedRunner.add(new CoreTestRunner());
        sharedRunner.add(new FeignRunner());
        sharedRunner.add(new InjectRunner());
        sharedRunner.add(new ContextCloseRunner());
        sharedRunner.add(new ContextCompleteRunner());
    }

    /**
     * 注册私有的Runner实例
     *
     * @param runner runner实例对象
     */
    public void register(Runner runner) {
        sharedRunner.add(runner);
    }

    /**
     * 根据给定被标识的class对象，获取当前能够被执行的{@link Runner}对象
     *
     * @param signedAnnotationClass 某个指定的
     * @return
     */
    public Runner.CoreRunner getRunner(Class<?> signedAnnotationClass) {
        Set<Running> mergedRunning = AnnotatedElementUtils.findAllMergedAnnotations(signedAnnotationClass, Running.class);

        // 构建CloseRunner列表
        List<Runner> registerRunners = Lists.newArrayList();
        registerRunners.addAll(sharedRunner.getSharedRegisterRunner());
        // @Running注解获取Runner
        List<Runner> runningRegisterRunner = mergedRunning.stream()
                .map(running -> sharedRunner.findSpecify(running.value()))
                // 不能分享
                .filter(runner -> RegisterRunner.class.isAssignableFrom(runner.getClass()) && !runner.shared())
                .collect(Collectors.toList());
        registerRunners.addAll(runningRegisterRunner);

        // 构建RefreshCompleteRunner列表
        List<Runner> refreshCompleteRunners = Lists.newArrayList();
        refreshCompleteRunners.addAll(sharedRunner.getSharedRefreshCompleteRunner());
        List<Runner> runningRefreshCompleteRunner = mergedRunning.stream()
                .map(running -> sharedRunner.findSpecify(running.value()))
                // 不能分享
                .filter(runner -> RefreshCompleteRunner.class.isAssignableFrom(runner.getClass()) && !runner.shared())
                .collect(Collectors.toList());
        refreshCompleteRunners.addAll(runningRefreshCompleteRunner);

        List<Runner> closeRunner = Lists.newArrayList();
        closeRunner.addAll(sharedRunner.getSharedContextCloseRunner());
        List<Runner> runningCloseRunner = mergedRunning.stream()
                .map(running -> sharedRunner.findSpecify(running.value()))
                // 不能分享
                .filter(runner -> CloseRunner.class.isAssignableFrom(runner.getClass()) && !runner.shared())
                .collect(Collectors.toList());
        closeRunner.addAll(runningCloseRunner);
        return new Runner.CoreRunner(registerRunners, refreshCompleteRunners, closeRunner);
    }

    /**
     * 获取单实例对象
     *
     * @return 单实例对象
     */
    public static RunnerCenter getSharedInstance() {
        RunnerCenter sharedInstance = instance;
        if (sharedInstance == null) {
            synchronized (RunnerCenter.class) {
                if (sharedInstance == null) {
                    instance = new RunnerCenter();
                    sharedInstance = instance;
                }
            }
        }
        return sharedInstance;
    }

    /**
     * Runner列表，支持根据Runner类型查找
     */
    public static class RunnerList extends HashSet<Runner> {

        /**
         * 在list中查找指定类型的{@link Runner}的列表
         *
         * @param clazz RunnerClass对象
         * @return 列表
         */
        public List<Runner> find(Class<? extends Runner> clazz) {
            Assert.notNull(clazz, String.format("Runner Clazz %s is not empty", clazz.getName()));
            return stream()
                    .filter(clazz::isInstance)
                    .collect(Collectors.toList());
        }

        /**
         * 在List中寻找指定类型的{@link Runner}
         *
         * @param clazz RunnerClass对象
         * @return 指定的Runner对象或者null
         */
        public Runner findSpecify(Class<? extends Runner> clazz) {
            Assert.notNull(clazz, String.format("Runner Clazz %s is not empty", clazz.getName()));
            return stream()
                    .filter(runner -> clazz.isAssignableFrom(runner.getClass()))
                    .findFirst()
                    .orElseThrow(() -> new NullPointerException(String.format("Runner List not find specify runner class[%s] instance", clazz.getName())));
        }

        /**
         * 获取可以被共享的{@link RegisterRunner}列表
         *
         * @return
         */
        public List<Runner> getSharedRegisterRunner() {
            return stream()
                    // 共享的
                    .filter(runner -> RegisterRunner.class.isAssignableFrom(runner.getClass()) && runner.shared())
                    .collect(Collectors.toList());
        }

        /**
         * 获取可以被共享的{@link RefreshCompleteRunner}列表
         *
         * @return
         */
        public List<Runner> getSharedRefreshCompleteRunner() {
            return stream()
                    // 共享的
                    .filter(runner -> RefreshCompleteRunner.class.isAssignableFrom(runner.getClass()) && runner.shared())
                    .collect(Collectors.toList());
        }

        /**
         * 获取可以被共享的{@link CloseRunner}列表
         *
         * @return
         */
        public List<Runner> getSharedContextCloseRunner() {
            return stream()
                    // 共享的
                    .filter(runner -> CloseRunner.class.isAssignableFrom(runner.getClass()) && runner.shared())
                    .collect(Collectors.toList());
        }
    }
}
