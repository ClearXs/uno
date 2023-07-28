package cc.allio.uno.component.sequential.wash;

import cc.allio.uno.component.sequential.context.DefaultSequentialContext;
import cc.allio.uno.component.sequential.washer.DefaultWasher;
import cc.allio.uno.component.sequential.washer.WashMachine;
import cc.allio.uno.component.sequential.washer.WasherAssembler;
import cc.allio.uno.component.sequential.TypeSequential;
import cc.allio.uno.component.sequential.UnCodeSequential;
import cc.allio.uno.core.type.MemoryTypeManager;
import cc.allio.uno.data.orm.config.ElasticSearchAutoConfiguration;
import cc.allio.uno.test.BaseTestCase;
import cc.allio.uno.test.RunTest;
import cc.allio.uno.test.env.annotation.EsEnv;
import cc.allio.uno.test.env.annotation.properties.EsProperties;
import com.google.common.collect.Maps;
import org.junit.jupiter.api.Test;

/**
 * 测试清洁装备器
 *
 * @author jiangwei
 * @date 2022/5/19 16:17
 * @since 1.0
 */
@RunTest(components = ElasticSearchAutoConfiguration.class)
@EsEnv
@EsProperties(uris = "http://43.143.195.208:9200")
class WasherAssemblerTest extends BaseTestCase {

    /**
     * Test Case: 测试没有装配清洁的数据
     */
    @Test
    void testNoAssemblerItem() {
        assertThrows(IllegalArgumentException.class, () -> WasherAssembler.motherBoard(new MemoryTypeManager()).assembleDefault().install());
    }

    @Test
    void testAssignWasher() {
        DefaultSequentialContext context = new DefaultSequentialContext(new TypeSequential(), Maps.newHashMap());
        WashMachine machine = WasherAssembler.motherBoard(new MemoryTypeManager()).pushItem(context).assembleAssignWasher().install();
        machine.contains(DefaultWasher.class);
    }


    @Test
    void testRecord() throws InterruptedException {
        DefaultSequentialContext context = new DefaultSequentialContext(new UnCodeSequential(), Maps.newHashMap());
        WashMachine machine = WasherAssembler.motherBoard(new MemoryTypeManager()).pushItem(context).assembleAssignWasher().install();
        machine.start();

        Thread.sleep(2000L);

    }
}
