package cc.allio.uno.data.orm.sql;

import cc.allio.uno.core.api.Key;
import cc.allio.uno.core.env.Envs;
import cc.allio.uno.data.orm.sql.ddl.SQLCreateTableOperator;
import cc.allio.uno.data.orm.sql.ddl.SQLDropTableOperator;
import cc.allio.uno.data.orm.sql.ddl.SQLExistTableOperator;
import cc.allio.uno.data.orm.sql.ddl.SQLShowColumnsOperator;
import cc.allio.uno.data.orm.sql.dml.SQLDeleteOperator;
import cc.allio.uno.data.orm.sql.dml.SQLInsertOperator;
import cc.allio.uno.data.orm.sql.dml.SQLQueryOperator;
import cc.allio.uno.data.orm.sql.dml.SQLUpdateOperator;
import cc.allio.uno.data.orm.type.DBType;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 操作管理接口
 *
 * @author jiangwei
 * @date 2023/4/13 18:52
 * @see SQLOperator
 * @see SQLOperatorFactory
 * @see DruidOperatorMetadata
 * @see LocalOperatorMetadata
 * @see ShardingSphereOperatorMetadata
 * @since 1.1.4
 */
public interface OperatorMetadata {

    String OPERATOR_METADATA_KEY = "allio.uno.data.orm.sql.operator";
    // 操作实现类型
    OperatorMetadataKey DRUID_OPERATOR_KEY = new OperatorMetadataKey("druid");
    OperatorMetadataKey LOCAL_OPERATOR_KEY = new OperatorMetadataKey("local");
    OperatorMetadataKey SHARDING_SPHERE_KEY = new OperatorMetadataKey("sharding-sphere");
    OperatorMetadataKey ELASTIC_SEARCH_KEY = new OperatorMetadataKey("elasticsearch");

    @Getter
    @AllArgsConstructor
    class OperatorMetadataKey implements Key {

        private final String key;

        @Override
        public String getProperties() {
            return OPERATOR_METADATA_KEY;
        }
    }

    /**
     * 获取系统配置下的operator key
     *
     * @return operator key or default  DRUID_OPERATOR_KEY
     * @see #DRUID_OPERATOR_KEY
     * @see #LOCAL_OPERATOR_KEY
     * @see #SHARDING_SPHERE_KEY
     * @see #ELASTIC_SEARCH_KEY
     */
    static OperatorMetadataKey getSystemOperatorKey() {
        String operatorKey = Envs.getProperty(OPERATOR_METADATA_KEY);
        if (DRUID_OPERATOR_KEY.getKey().equals(operatorKey)) {
            return DRUID_OPERATOR_KEY;
        } else if (LOCAL_OPERATOR_KEY.getKey().equals(operatorKey)) {
            return LOCAL_OPERATOR_KEY;
        } else if (SHARDING_SPHERE_KEY.getKey().equals(operatorKey)) {
            return SHARDING_SPHERE_KEY;
        } else if (ELASTIC_SEARCH_KEY.getKey().equals(operatorKey)) {
            return ELASTIC_SEARCH_KEY;
        }
        return DRUID_OPERATOR_KEY;
    }

    // ======================== DML ========================

    /**
     * 获取查询操作
     *
     * @return QueryOperator
     */
    default SQLQueryOperator query() {
        return query(DBType.getSystemDbType());
    }

    /**
     * 获取查询操作
     *
     * @param dbType dbType
     * @return QueryOperator
     */
    SQLQueryOperator query(DBType dbType);

    /**
     * 获取insert操作
     *
     * @return InsertOperator
     */
    default SQLInsertOperator insert() {
        return insert(DBType.getSystemDbType());
    }

    /**
     * 获取insert操作
     *
     * @param dbType dbType
     * @return InsertOperator
     */
    SQLInsertOperator insert(DBType dbType);

    /**
     * 获取update操作
     *
     * @return SQLUpdateOperator
     */
    default SQLUpdateOperator update() {
        return update(DBType.getSystemDbType());
    }

    /**
     * 获取update操作
     *
     * @param dbType dbType
     * @return SQLUpdateOperator
     */
    SQLUpdateOperator update(DBType dbType);


    /**
     * 获取delete操作
     *
     * @return SQLDeleteOperator
     */
    default SQLDeleteOperator delete() {
        return delete(DBType.getSystemDbType());
    }

    /**
     * 获取delete操作
     *
     * @return SQLDeleteOperator
     */
    SQLDeleteOperator delete(DBType dbType);

    // ======================== DDL ========================

    /**
     * create table操作
     *
     * @return SQLCreateTableOperator
     */
    default SQLCreateTableOperator createTable() {
        return createTable(DBType.getSystemDbType());
    }

    /**
     * create table操作
     *
     * @param dbType dbType
     * @return SQLCreateTableOperator
     */
    SQLCreateTableOperator createTable(DBType dbType);

    /**
     * drop table
     *
     * @return SQLDropTableOperator
     */
    default SQLDropTableOperator dropTable() {
        return dropTable(DBType.getSystemDbType());
    }

    /**
     * drop table
     *
     * @param dbType dbType
     * @return SQLDropTableOperator
     */
    SQLDropTableOperator dropTable(DBType dbType);

    /**
     * exist table
     *
     * @return SQLExistTableOperator
     */
    default SQLExistTableOperator existTable() {
        return existTable(DBType.getSystemDbType());
    }

    /**
     * exist table
     *
     * @param dbType dbType
     * @return SQLExistTableOperator
     */
    SQLExistTableOperator existTable(DBType dbType);

    /**
     * show columns for table
     *
     * @return SQLShowColumnsOperator
     */
    default SQLShowColumnsOperator showColumns() {
        return showColumns(DBType.getSystemDbType());
    }

    /**
     * show columns for table
     *
     * @param dbType dbType
     * @return SQLShowColumnsOperator
     */
    SQLShowColumnsOperator showColumns(DBType dbType);

    /**
     * 获取 OperatorMetadata key
     *
     * @return OperatorMetadataKey
     */
    OperatorMetadataKey getKey();
}
