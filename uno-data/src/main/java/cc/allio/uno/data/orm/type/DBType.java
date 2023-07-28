package cc.allio.uno.data.orm.type;

import cc.allio.uno.core.env.Envs;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Objects;

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

    DBType MYSQL = new DefaultDBType("MySQL", DBCategory.RELATIONAL);
    DBType POSTGRESQL = new DefaultDBType("PostgreSQL", DBCategory.RELATIONAL);
    DBType SQLSERVER = new DefaultDBType("SQLServer", DBCategory.RELATIONAL);
    DBType ORACLE = new DefaultDBType("Oracle", DBCategory.RELATIONAL);
    DBType OPEN_GAUSS = new DefaultDBType(" OpenGauss", DBCategory.RELATIONAL);
    DBType SQL_92 = new DefaultDBType("SQL-92", DBCategory.RELATIONAL);
    DBType H2 = new DefaultDBType("H2", DBCategory.RELATIONAL);
    DBType ELASTIC_SEARCH = new DefaultDBType("ElasticSearch", DBCategory.SEARCH_ENGINES);
    DBType TD_ENGINE = new DefaultDBType("TDEngine", DBCategory.TIME_SERIES);
    DBType INFLUXDB = new DefaultDBType("Influxdb", DBCategory.TIME_SERIES);

    /**
     * 类型集合
     */
    List<DBType> TYPE_SETS = Lists.newArrayList(MYSQL, POSTGRESQL, SQLSERVER, ORACLE, OPEN_GAUSS, SQL_92, H2, ELASTIC_SEARCH, TD_ENGINE, INFLUXDB);

    /**
     * 获取数据库类型名称
     *
     * @return 数据库全名称
     */
    String getName();

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
        return TYPE_SETS.stream()
                .filter(dbType -> dbType.getName().equals(dbtype))
                .findFirst()
                .orElse(H2);
    }

    /**
     * 数据库类型分类
     *
     * @see <a href="https://db-engines.com/en/ranking">see</a>
     */
    enum DBCategory {
        RELATIONAL, KEY_VALUE, DOCUMENT, TIME_SERIES, GRAPH, SEARCH_ENGINES, OBJECT_ORIENTED
    }

    class DefaultDBType implements DBType {
        private final String name;
        private final DBCategory category;

        public DefaultDBType(String name, DBCategory category) {
            this.name = name;
            this.category = category;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public DBCategory getCategory() {
            return category;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DefaultDBType that = (DefaultDBType) o;
            return Objects.equals(name, that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }
}
