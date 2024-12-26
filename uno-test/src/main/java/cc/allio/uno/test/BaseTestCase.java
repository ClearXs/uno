package cc.allio.uno.test;

import cc.allio.uno.core.exception.Exceptions;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

/**
 * 基于<b>jupiter单元测试框架</b>{@code TestCode}提供来标准化的测试基准：</br>
 * <ol>
 *     <li>继承于这个基类</li>
 *     <li>初始化的<b>fixture</b>{{@link #setup()}}</li>
 *     <li>在结束后的<b>fixture</b>{@link #tearDown()}</li>
 * </ol>
 * 除此之后还支持不同框架（如Redis、Spring）的环境支持
 *
 * @author j.x
 * @see BaseSpringTest
 * @since 1.0
 */
@Slf4j
public abstract class BaseTestCase extends Assertions implements BenchmarkTest {

    private long currentTime;

    @Override
    @BeforeEach
    public void setup() {
        try {
            onInit();
        } catch (Throwable ex) {
            throw Exceptions.unchecked(ex);
        }
    }

    @Override
    @AfterEach
    public void tearDown() {
        try {
            onDown();
        } catch (Throwable ex) {
            throw Exceptions.unchecked(ex);
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
