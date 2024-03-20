package cc.allio.uno.test.testcontainers;

import cc.allio.uno.test.CoreTest;
import cc.allio.uno.test.Parameter;
import cc.allio.uno.test.RunTest;
import org.junit.jupiter.api.Test;

@RunTest
@RunContainer(ContainerType.PostgreSQL)
class PostgreSQLTest {

    @Test
    void testPg(@Parameter CoreTest coreTest) {
        Container container = coreTest.getContainer();

    }
}
