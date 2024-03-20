package cc.allio.uno.rule.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * rule properties
 *
 * @author j.x
 * @date 2023/4/25 15:54
 * @since 1.1.4
 */
@Data
@ConfigurationProperties(prefix = "allio.uno.rule")
public class RuleProperties {

    private Engine engine = new Engine();

    @Data
    public static class Engine {
        private String type = "drools";
    }
}
