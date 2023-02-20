package cc.allio.uno.data.reactive.repository;

import cc.allio.uno.data.reactive.R2dbcConfiguration;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.ContextConfiguration;

@DataR2dbcTest
@ContextConfiguration(classes = R2dbcConfiguration.class)
public class CarReactiveRepositoryTest {

}
