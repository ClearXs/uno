package cc.allio.uno.test;

import cc.allio.uno.test.env.TestSpringEnvironment;
import cc.allio.uno.test.env.TestSpringEnvironmentFacade;
import cc.allio.uno.test.runner.Runner;
import cc.allio.uno.test.runner.RunnerCenter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 提供uno.core下基础的Spring环境
 *
 * @author jiangwei
 * @date 2022/2/14 14:03
 * @since 1.0
 */
@Slf4j
public abstract class BaseCoreTest extends BaseSpringTest {

    private TestSpringEnvironmentFacade env;

    /**
     * 测试执行实例对象
     */
    Runner.CoreRunner coreRunner;

    @Override
    protected void onInitSpringEnv() throws Throwable {
        onEnvBuild();
        setCoreRunner(RunnerCenter.getSharedInstance().getRunner(this.getClass()));
        Collection<Runner> registerRunner = getCoreRunner().getRegisterRunner();
        for (Runner runner : registerRunner) {
            try {
                runner.run(this);
            } catch (Throwable ex) {
                // ignore Exception
                log.error("runner: {} run is failed", runner, ex);
            }
        }
    }

    @Override
    protected void onRefreshComplete() throws Throwable {
        for (Runner runner : getCoreRunner().getRefreshCompleteRunner()) {
            try {
                runner.run(this);
            } catch (Throwable ex) {
                // ignore Exception
                log.error("runner: {} run is failed", runner, ex);
            }
        }
    }

    /**
     * 触发uno-core环境构建事件，子类实现。
     * 模板方法
     */
    protected void onEnvBuild() {

    }

    @Override
    protected void onContextClose() throws Throwable {
        for (Runner runner : getCoreRunner().getCloseRunner()) {
            try {
                runner.run(this);
            } catch (Throwable ex) {
                // ignore Exception
                log.error("runner: {} run is failed", runner, ex);
            }
        }
    }

    /**
     * 子类提供Spring环境类
     * 模板方法
     *
     * @return 环境类实例
     */
    public TestSpringEnvironment supportEnv() {
        return null;
    }

    // ----------------- get/set -----------------

    public void setEnv(TestSpringEnvironmentFacade env) {
        this.env = env;
    }

    /**
     * 获取当前测试的环境实例
     *
     * @return
     */
    public TestSpringEnvironmentFacade getEnv() {
        return env;
    }

    public void setCoreRunner(Runner.CoreRunner coreRunner) {
        this.coreRunner = coreRunner;
    }

    public Runner.CoreRunner getCoreRunner() {
        return coreRunner;
    }
}
