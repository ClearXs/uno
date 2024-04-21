package cc.allio.uno.data.orm.config.influxdb;

import com.influxdb.LogLevel;
import com.influxdb.client.domain.WritePrecision;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * describe influxdb properties
 *
 * @author j.x
 * @date 2024/4/14 18:16
 * @since 1.1.8
 */
@Data
@ConfigurationProperties(prefix = "allio.uno.data.influxdb")
public class InfluxdbProperties {

    public static final String ORGANIZATION = "organization";
    public static final String LOG_LEVEL = "logLevel";
    public static final String WRITE_PRECISION = "writePrecision";
    public static final String TOKEN = "token";

    /**
     * enable influxdb
     */
    private Boolean enabled = false;

    /**
     * is system default command executor
     */
    private Boolean systemDefault = false;

    /**
     * influxdb address, like as localhost:8086
     */
    private String address = "http://localhost:8086";

    /**
     * influxdb organization
     */
    private String organization;

    /**
     * influxdb bucket
     */
    private String bucket;

    /**
     * auth for username
     */
    private String username;

    /**
     * auth for password
     */
    private String password;

    /**
     * the authentication token
     */
    private String token;

    /**
     * log level
     */
    private LogLevel logLevel = LogLevel.BASIC;

    /**
     * write precision, default is seconds
     */
    private WritePrecision writePrecision = WritePrecision.S;
}
