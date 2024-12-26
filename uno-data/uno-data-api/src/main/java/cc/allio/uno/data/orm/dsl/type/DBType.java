package cc.allio.uno.data.orm.dsl.type;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.env.Envs;
import cc.allio.uno.core.util.template.ExpressionTemplate;
import cc.allio.uno.core.util.template.Tokenizer;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.boot.jdbc.DatabaseDriver;

import java.util.List;

/**
 * 定义数据库类型
 *
 * @author j.x
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

    ExpressionTemplate JDBC_URL_TEMPLATE_PARSER = ExpressionTemplate.createTemplate(Tokenizer.HASH_BRACE);
    String ADDRESS = "address";
    String IP_PORTS_TEMPLATE = "#{" + ADDRESS + "}";
    String DATABASE_NAME = "database_name";
    String DATABASE_NAME_TEMPLATE = "#{" + DATABASE_NAME + "}";

    DBType MYSQL = new DefaultDBType("MySQL", DatabaseDriver.MYSQL.getDriverClassName(), DBCategory.RELATIONAL, "jdbc:mysql://" + IP_PORTS_TEMPLATE + "/" + DATABASE_NAME_TEMPLATE + "?useSSL=false&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&tinyInt1isBit=false&allowMultiQueries=true&serverTimezone=GMT%2B8&allowPublicKeyRetrieval=true");
    DBType POSTGRESQL = new DefaultDBType("PostgreSQL", DatabaseDriver.POSTGRESQL.getDriverClassName(), DBCategory.RELATIONAL, "jdbc:postgresql://" + IP_PORTS_TEMPLATE + "/" + DATABASE_NAME_TEMPLATE + "?stringtype=unspecified");
    DBType SQLSERVER = new DefaultDBType("SQLServer", DatabaseDriver.SQLSERVER.getDriverClassName(), DBCategory.RELATIONAL, "jdbc:sqlserver://" + IP_PORTS_TEMPLATE + ";database=" + DATABASE_NAME_TEMPLATE);
    DBType ORACLE = new DefaultDBType("Oracle", DatabaseDriver.ORACLE.getDriverClassName(), DBCategory.RELATIONAL, "jdbc:oracle:thin:@//" + IP_PORTS_TEMPLATE + "/" + DATABASE_NAME_TEMPLATE);
    DBType OPEN_GAUSS = new DefaultDBType(" OpenGauss", "org.opengauss.Driver", DBCategory.RELATIONAL, "");
    DBType DB2 = new DefaultDBType(" Db2", DatabaseDriver.DB2.getDriverClassName(), DBCategory.RELATIONAL, "jdbc:db2://" + IP_PORTS_TEMPLATE + "/" + DATABASE_NAME_TEMPLATE);
    DBType MARIADB = new DefaultDBType("MariaDB", DatabaseDriver.MARIADB.getDriverClassName(), DBCategory.RELATIONAL, "jdbc:mariadb://" + IP_PORTS_TEMPLATE + "/" + DATABASE_NAME_TEMPLATE);
    DBType SQLITE = new DefaultDBType("SQLite", DatabaseDriver.SQLITE.getDriverClassName(), DBCategory.RELATIONAL, "jdbc:sqlite::memory:");
    DBType H2 = new DefaultDBType("H2", DatabaseDriver.H2.getDriverClassName(), DBCategory.RELATIONAL, "jdbc:h2:mem:" + DATABASE_NAME_TEMPLATE + ";IGNORECASE=TRUE");
    DBType ELASTICSEARCH = new DefaultDBType("ElasticSearch", StringPool.EMPTY, DBCategory.SEARCH_ENGINES, "");
    DBType MONGODB = new DefaultDBType("Mongodb", "org.mongodb.Driver", DBCategory.DOCUMENT, "");
    DBType TD_ENGINE = new DefaultDBType("TDEngine", StringPool.EMPTY, DBCategory.TIME_SERIES, "");
    DBType INFLUXDB = new DefaultDBType("Influxdb", StringPool.EMPTY, DBCategory.TIME_SERIES, "");
    DBType NEO4J = new DefaultDBType("Neo4j", StringPool.EMPTY, DBCategory.GRAPH, "");
    DBType REDIS = new DefaultDBType("Redis", StringPool.EMPTY, DBCategory.KEY_VALUE, "");

    /**
     * 类型集合
     */
    List<DBType> ALL_DB_TYPES = Lists.newArrayList(MYSQL, POSTGRESQL, SQLSERVER, ORACLE, OPEN_GAUSS, DB2, MARIADB, SQLITE, H2, ELASTICSEARCH, MONGODB, TD_ENGINE, INFLUXDB, NEO4J, REDIS);

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
     * 解析jdbcUrl模板
     *
     * @param address      连接ip与端口，如果是集群注意需按照对应集群的ip端口序列写法，比如:'192.168.2.1:3306,192.168.2.2:3306'
     * @param dataBaseName 数据名称
     * @return 解析后的模板
     */
    String parseTemplate(String address, String dataBaseName);

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
        private final String jdbcUrlTemplate;

        @Override
        public String parseTemplate(String address, String dataBaseName) {
            return JDBC_URL_TEMPLATE_PARSER.parseTemplate(getJdbcUrlTemplate(), ADDRESS, address, DATABASE_NAME, dataBaseName);
        }
    }
}
