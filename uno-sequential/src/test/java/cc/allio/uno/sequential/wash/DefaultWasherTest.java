package cc.allio.uno.sequential.wash;

import cc.allio.uno.sequential.TypeSequential;
import cc.allio.uno.sequnetial.context.DefaultSequentialContext;
import cc.allio.uno.sequnetial.washer.DefaultWasher;
import cc.allio.uno.test.BaseTestCase;
import com.google.common.collect.Maps;
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
        boolean test = washer.cleaning().test(new DefaultSequentialContext(testSequential, Maps.newHashMap()));
        assertTrue(test);
        testSequential.setSequentialId(null);
        test = washer.cleaning().test(new DefaultSequentialContext(testSequential, Maps.newHashMap()));
        assertFalse(test);
    }

}
