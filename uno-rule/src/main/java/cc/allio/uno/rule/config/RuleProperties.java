package cc.allio.uno.rule.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * rule properties
 *
 * @author j.x
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
