package cc.allio.uno.core;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

@Slf4j
public abstract class BaseTestCase extends Assertions implements BenchmarkTest {

    private long currentTime;

    @Override
    @BeforeEach
    public void setup() {
        try {
            onInit();
        } catch (Throwable e) {
            log.error("Unit test init failed", e);
        }
    }

    @Override
    @AfterEach
    public void tearDown() {
        try {
            onDown();
        } catch (Throwable e) {
            log.error("Unit test down failed", e);
        }
    }

    /**
     * 计时
     */
    protected void timing() {
        currentTime = System.currentTimeMillis();
    }

    /**
     * 结束计时
     */
    protected void stopTiming() {
        log.info("record Consumption time: {}", System.currentTimeMillis() - currentTime);
    }

    /**
     * 子类继承时进行实现的，在每个测试前执行，详细可以看{@link BeforeEach}注解<br/>
     * 如果是全部单侧前执行的话则看{@link BeforeAll}
     *
     * @throws Throwable 某些初始化动作失败时抛出，如JDBC的连接
     */
    protected void onInit() throws Throwable {
    }

    /**
     * 字类继承时进行实现，在每个测试完成后执行，详细看{@link AfterEach}注解<br/>
     * 如果时全部单侧执行后完后执行看{@link AfterAll}
     *
     * @throws Throwable 某些单侧结束后时抛出，如JDBC连接的关闭
     */
    protected void onDown() throws Throwable {
    }
}
