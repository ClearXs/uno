package cc.allio.uno.data.orm.config.elasticsearch;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * describe elasticsearch properties
 *
 * @author j.x
 * @date 2024/4/14 19:59
 * @since 1.1.8
 */
@Data
@ConfigurationProperties(prefix = "allio.uno.data.elasticsearch")
public class ElasticSearchProperties {

    /**
     * enable influxdb
     */
    private Boolean enabled = false;

    /**
     * influxdb address, like as localhost:27017
     */
    private String address = "localhost:9200";

    /**
     * auth for username
     */
    private String username;

    /**
     * auth for password
     */
    private String password;

    /**
     * database
     */
    private String database;

    /**
     * is system default command executor
     */
    private Boolean systemDefault = true;
}
