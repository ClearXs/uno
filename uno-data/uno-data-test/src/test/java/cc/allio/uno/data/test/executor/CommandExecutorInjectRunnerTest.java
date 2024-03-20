package cc.allio.uno.data.test.executor;

import cc.allio.uno.test.BaseTestCase;
import cc.allio.uno.test.RunTest;
import cc.allio.uno.test.testcontainers.ContainerType;
import cc.allio.uno.test.testcontainers.RunContainer;
import org.junit.jupiter.api.Test;

@RunTest
@RunContainer(ContainerType.Test)
class CommandExecutorInjectRunnerTest extends BaseTestCase implements CommandExecutorSetter<FakeCommandExecutor> {

    private FakeCommandExecutor executor;

    @Test
    void testSetCommandIsNotNull() {
        assertNotNull(executor);
    }

    @Override
    public void setCommandExecutor(FakeCommandExecutor executor) {
        this.executor = executor;
    }
}
