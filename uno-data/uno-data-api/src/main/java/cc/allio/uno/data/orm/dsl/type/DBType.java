package cc.allio.uno.data.orm.dsl.type;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.env.Envs;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

/**
 * 定义数据库类型
 *
 * @author jiangwei
 * @date 2023/2/23 16:36
 * @since 1.1.4
 */
public interface DBType {

    /**
     * 数据库类型配置标识
     *
     * @see #MYSQL
     * @see #POSTGRESQL
     */
    String DB_TYPE_CONFIG_KEY = "allio.uno.data.orm.dbtype";

    DBType MYSQL = new DefaultDBType("MySQL", "com.mysql.cj.jdbc.Driver", DBCategory.RELATIONAL);
    DBType POSTGRESQL = new DefaultDBType("PostgreSQL", "org.postgresql.Driver", DBCategory.RELATIONAL);
    DBType SQLSERVER = new DefaultDBType("SQLServer", "com.microsoft.sqlserver.jdbc.SQLServerDriver", DBCategory.RELATIONAL);
    DBType ORACLE = new DefaultDBType("Oracle", "oracle.jdbc.driver.OracleDriver", DBCategory.RELATIONAL);
    DBType OPEN_GAUSS = new DefaultDBType(" OpenGauss", "org.opengauss.Driver", DBCategory.RELATIONAL);
    DBType DB2 = new DefaultDBType(" Db2", "com.ibm.db2.jdbc.app.DB2Driver", DBCategory.RELATIONAL);
    DBType MARIADB = new DefaultDBType("MariaDB", "org.mariadb.jdbc.Driver", DBCategory.RELATIONAL);
    DBType SQLITE = new DefaultDBType("SQLite", "org.sqlite.JDBC", DBCategory.RELATIONAL);
    DBType H2 = new DefaultDBType("H2", "org.h2.Driver", DBCategory.RELATIONAL);
    DBType ELASTIC_SEARCH = new DefaultDBType("ElasticSearch", StringPool.EMPTY, DBCategory.SEARCH_ENGINES);
    DBType MONGODB = new DefaultDBType("Mongodb", "org.mongodb.Driver", DBCategory.DOCUMENT);
    DBType TD_ENGINE = new DefaultDBType("TDEngine", StringPool.EMPTY, DBCategory.TIME_SERIES);
    DBType INFLUXDB = new DefaultDBType("Influxdb", StringPool.EMPTY, DBCategory.TIME_SERIES);
    DBType NEO4J = new DefaultDBType("Neo4j", StringPool.EMPTY, DBCategory.GRAPH);
    DBType REDIS = new DefaultDBType("Redis", StringPool.EMPTY, DBCategory.KEY_VALUE);

    /**
     * 类型集合
     */
    List<DBType> ALL_DB_TYPES = Lists.newArrayList(MYSQL, POSTGRESQL, SQLSERVER, ORACLE, OPEN_GAUSS, DB2, MARIADB, SQLITE, H2, ELASTIC_SEARCH, MONGODB, TD_ENGINE, INFLUXDB, NEO4J, REDIS);

    /**
     * 获取数据库类型名称
     *
     * @return 数据库全名称
     */
    String getName();

    /**
     * 如果存在则获取对应的驱动类名
     */
    String getDriverClassName();

    /**
     * 数据库类别
     *
     * @return DBCategory
     */
    DBCategory getCategory();

    /**
     * 获取当前项目中的数据库类型。多数据源采用主数据源作为数据库类型
     *
     * @return DBType or H2数据库
     */
    static DBType getSystemDbType() {
        String dbtype = Envs.getProperty(DB_TYPE_CONFIG_KEY);
        try {
            return getDbType(dbtype);
        } catch (IllegalArgumentException ex) {
            // ignore
            return H2;
        }
    }

    /**
     * 根据dbtype的字符串获取DBType。
     *
     * @param dbtype dbtype
     * @return DBType default {@link #H2}
     */
    static DBType getDbType(String dbtype) {
        return ALL_DB_TYPES.stream()
                .filter(dbType -> dbType.getName().equals(dbtype))
                .findFirst()
                .orElse(H2);
    }

    /**
     * 数据库类型分类
     *
     * @see <a href="https://db-engines.com/en/ranking">see</a>
     */
    @Getter
    @AllArgsConstructor
    enum DBCategory {
        RELATIONAL("Relational", "传统关系型数据库"),
        KEY_VALUE("key Value", "键值对数据库"),
        DOCUMENT("Document", "文档数据库"),
        TIME_SERIES("Time Series", "时序数据库"),
        GRAPH("Graph", "图数据库"),
        SEARCH_ENGINES("Search Engines", "搜索引擎"),
        OBJECT_ORIENTED("Object Oriented", "面对对象数据库"),
        VECTOR("Vector", "向量数据库");

        private final String name;
        private final String label;
    }

    @Getter
    @EqualsAndHashCode(of = "name")
    @AllArgsConstructor
    class DefaultDBType implements DBType {

        private final String name;
        private final String driverClassName;
        private final DBCategory category;

    }
}
