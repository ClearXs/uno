package cc.allio.uno.data.orm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ORM Properties
 *
 * @author jiangwei
 * @date 2023/4/16 23:42
 * @since 1.1.4
 */
@Data
@ConfigurationProperties(prefix = "allio.uno.data.orm")
public class ORMProperties {

    private SQL sql = new SQL();
    private Executor executor = new Executor();

    @Data
    public static class SQL {
        private SQLOperatorMetadata operator = SQLOperatorMetadata.DRUID;
        private SQLFormat format = SQLFormat.UNDERLINE;
    }

    @Data
    public static class Executor {
        private SQLExecutor type = SQLExecutor.MYBATIS;
    }

    @Getter
    @AllArgsConstructor
    public enum SQLOperatorMetadata {
        DRUID("druid"),
        LOCAL("local"),
        SHARDING_SPHERE("sharding-sphere");
        private final String name;
    }

    @Getter
    @AllArgsConstructor
    public enum SQLExecutor {
        MYBATIS("mybatis");
        private final String name;
    }

    @Getter
    @AllArgsConstructor
    public enum SQLFormat {
        UNDERLINE("underline"),
        HUMP("HUMP_FEATURE");
        private final String name;
    }
}
