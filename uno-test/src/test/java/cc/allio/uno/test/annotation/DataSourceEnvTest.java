package cc.allio.uno.test.annotation;

import cc.allio.uno.test.Inject;
import cc.allio.uno.test.RunTest;
import cc.allio.uno.test.env.annotation.DataSourceEnv;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

@RunTest
@DataSourceEnv
public class DataSourceEnvTest {

    @Inject
    private  JdbcTemplate jdbcTemplate;

    @Test
    void test() {
        Assertions.assertNotNull(jdbcTemplate);
    }
}
