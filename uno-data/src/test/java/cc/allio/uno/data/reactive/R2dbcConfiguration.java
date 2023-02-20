package cc.allio.uno.data.reactive;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EntityScan("cc.allio.uno.uno")
@EnableR2dbcRepositories("cc.allio.uno.uno.data.reactive")
public class R2dbcConfiguration {
}
