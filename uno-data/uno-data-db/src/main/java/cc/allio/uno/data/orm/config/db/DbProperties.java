package cc.allio.uno.data.orm.config.db;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * describe database command executor type properties
 *
 * @author j.x
 * @date 2024/4/14 19:23
 * @since 1.1.8
 */
@Data
@ConfigurationProperties(prefix = "allio.uno.data.db")
public class DbProperties {

    /**
     * enable influxdb
     */
    private Boolean enabled = false;

    /**
     * database type
     */
    private DbType dbType = DbType.H2;

    /**
     * influxdb address, like as localhost:3306
     */
    private String address;

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

    public enum DbType {
        MYSQL,
        POSTGRESQL,
        SQLSERVER,
        ORACLE,
        OPEN_GAUSS,
        DB2,
        MARIADB,
        SQLITE,
        H2
    }
}
