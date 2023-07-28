package cc.allio.uno.test.runner;

import com.google.common.collect.Lists;
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

    /**
     * 可以被共享的Runner实例
     */
    public final RunnerList sharedRunner = new RunnerList();

    public RunnerCenter() {
    }

    /**
     * 注册私有的Runner实例
     *
     * @param runner runner实例对象
     */
    public void register(Runner... runner) {
        sharedRunner.addAll(Lists.newArrayList(runner));
    }

    /**
     * 注册私有的Runner实例
     *
     * @param runners runner实例对象
     */
    public void register(Collection<Runner> runners) {
        sharedRunner.addAll(runners);
    }

    /**
     * 根据给定被标识的class对象，获取当前能够被执行的{@link Runner}对象
     *
     * @return CoreRunner
     */
    public CoreRunner getRunner() {
        List<Runner> registerRunners = Lists.newArrayList();
        registerRunners.addAll(sharedRunner.getSharedRegisterRunner());

        // 构建RefreshCompleteRunner列表
        List<Runner> refreshCompleteRunners = Lists.newArrayList();
        refreshCompleteRunners.addAll(sharedRunner.getSharedRefreshCompleteRunner());

        // 构建CloseRunner列表
        List<Runner> closeRunner = Lists.newArrayList();
        closeRunner.addAll(sharedRunner.getSharedContextCloseRunner());
        return new CoreRunner(registerRunners, refreshCompleteRunners, closeRunner);
    }

    /**
     * 清空所有的Runner
     */
    public void clear() {
        sharedRunner.clear();
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
         * @return List<Runner>
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
         * @return List<Runner>
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
         * @return List<Runner>
         */
        public List<Runner> getSharedContextCloseRunner() {
            return stream()
                    // 共享的
                    .filter(runner -> CloseRunner.class.isAssignableFrom(runner.getClass()) && runner.shared())
                    .collect(Collectors.toList());
        }
    }
}
