package cc.allio.uno.component.sequential.wash;

import cc.allio.uno.component.sequential.TypeSequential;
import cc.allio.uno.component.sequential.washer.DefaultWasher;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

/**
 * 测试默认清洗器
 *
 * @author jiangwei
 * @date 2022/5/19 16:54
 * @since 1.0
 */
class DefaultWasherTest extends BaseTestCase {

    @Test
    void testCleaning() {
        DefaultWasher washer = new DefaultWasher();
        TypeSequential testSequential = new TypeSequential();
        testSequential.setSequentialId(1L);
        boolean test = washer.cleaning().test(testSequential);
        assertTrue(test);
        testSequential.setSequentialId(null);
        test = washer.cleaning().test(testSequential);
        assertFalse(test);
    }

    @Override
    protected void onInit() throws Throwable {

    }

    @Override
    protected void onDown() throws Throwable {

    }
}
