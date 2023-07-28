package cc.allio.uno.test;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Service;

@RunServiceTest(mapperScan = RunServiceWithTest.TestMapperScan.class)
@RunTest(profile = "application")
class RunServiceWithTest extends CoreTest {

    @Test
    void testGetTestService() {
        assertDoesNotThrow(() -> getBean(TestService.class));
    }

    @Service
    static class TestService {

    }

    @MapperScan("xxx")
    static class TestMapperScan {

    }
}
