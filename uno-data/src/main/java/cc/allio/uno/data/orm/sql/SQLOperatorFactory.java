package cc.allio.uno.data.orm.sql;

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
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * {@link SQLOperator}的生成工厂（静态工厂）
 *
 * @author jiangwei
 * @date 2023/4/16 13:10
 * @see SQLCreateTableOperator
 * @see SQLDropTableOperator
 * @see SQLInsertOperator
 * @see SQLQueryOperator
 * @since 1.1.4
 */
public class SQLOperatorFactory {

    private SQLOperatorFactory() {
    }

    private static final Map<OperatorMetadata.OperatorMetadataKey, OperatorMetadata> CACHES = Maps.newHashMapWithExpectedSize(3);

    /**
     * 获取当前系统的SQL Operator
     *
     * @return OperatorMetadata or DruidOperatorMetadata（默认为druid）
     */
    public static <T extends OperatorMetadata> T getSystemOperatorMetadata() {
        return getOperatorMetadata(OperatorMetadata.getSystemOperatorKey());
    }

    /**
     * 获取当前系统的SQL Operator
     *
     * @return OperatorMetadata or DruidOperatorMetadata（默认为druid）
     */
    public static <T extends OperatorMetadata> T getOperatorMetadata(OperatorMetadata.OperatorMetadataKey operatorKey) {
        if (OperatorMetadata.DRUID_OPERATOR_KEY.equals(operatorKey)) {
            return (T) CACHES.computeIfAbsent(operatorKey, k -> new DruidOperatorMetadata());
        } else if (OperatorMetadata.SHARDING_SPHERE_KEY.equals(operatorKey)) {
            return (T) CACHES.computeIfAbsent(operatorKey, k -> new ShardingSphereOperatorMetadata());
        } else if (OperatorMetadata.LOCAL_OPERATOR_KEY.equals(operatorKey)) {
            return (T) CACHES.computeIfAbsent(operatorKey, k -> new LocalOperatorMetadata());
        } else if (OperatorMetadata.ELASTIC_SEARCH_KEY.equals(operatorKey)) {
            return (T) CACHES.computeIfAbsent(operatorKey, k -> new ElasticSearchOperatorMetadata());
        }
        // 系统配置中不存在uno.data.orm.sql.operator属性，设置默认
        Envs.setProperty(OperatorMetadata.OPERATOR_METADATA_KEY, OperatorMetadata.DRUID_OPERATOR_KEY.getKey());
        // 放入缓存中
        DruidOperatorMetadata druidOperatorMetadata = new DruidOperatorMetadata();
        CACHES.put(OperatorMetadata.DRUID_OPERATOR_KEY, druidOperatorMetadata);
        return (T) druidOperatorMetadata;
    }

    /**
     * 根据operator的class获取指定operator实例.
     * <ul>
     *     <li>operator key = {@link OperatorMetadata#getSystemOperatorKey()}</li>
     *     <li>dbtype = {@link DBType#getSystemDbType()}</li>
     * </ul>
     *
     * @param operatorClass operatorClass
     * @param <T>           SQLOperator
     * @return SQLOperator
     * @throws IllegalArgumentException operatorClass is null
     * @throws NullPointerException     An operation that does not exist
     */
    public static <T extends SQLOperator<T>> T getSQLOperator(Class<T> operatorClass) {
        return getSQLOperator(operatorClass, OperatorMetadata.getSystemOperatorKey(), DBType.getSystemDbType());
    }

    /**
     * 根据operator的class获取指定operator实例
     * <ul>
     *     <li>dbtype = {@link DBType#getSystemDbType()}</li>
     * </ul>
     *
     * @param operatorClass operatorClass
     * @param <T>           SQLOperator
     * @param operatorKey   operatorKey
     * @return SQLOperator
     * @throws IllegalArgumentException operatorClass is null
     * @throws NullPointerException     An operation that does not exist
     * @see OperatorMetadata#getSystemOperatorKey()
     */
    public static <T extends SQLOperator<T>> T getSQLOperator(Class<T> operatorClass, OperatorMetadata.OperatorMetadataKey operatorKey) {
        return getSQLOperator(operatorClass, operatorKey, DBType.getSystemDbType());
    }

    /**
     * 根据operator的class获取指定operator实例
     * <ul>
     *     <li>dbtype = {@link DBType#getSystemDbType()}</li>
     * </ul>
     *
     * @param operatorClass operatorClass
     * @param <T>           SQLOperator
     * @param dbType        dbType
     * @return SQLOperator
     * @throws IllegalArgumentException operatorClass is null
     * @throws NullPointerException     An operation that does not exist
     * @see OperatorMetadata#getSystemOperatorKey()
     */
    public static <T extends SQLOperator<T>> T getSQLOperator(Class<T> operatorClass, DBType dbType) {
        return getSQLOperator(operatorClass, OperatorMetadata.getSystemOperatorKey(), dbType);
    }

    /**
     * 根据operator的class获取指定operator实例
     *
     * @param operatorClass operatorClass
     * @param operatorKey   operatorKey
     * @param dbType        dbtype
     * @param <T>           SQLOperator
     * @return SQLOperator
     * @throws IllegalArgumentException operatorClass is null
     * @throws NullPointerException     An operation that does not exist
     * @see OperatorMetadata.OperatorMetadataKey
     */
    public static <T extends SQLOperator<T>> T getSQLOperator(Class<T> operatorClass, OperatorMetadata.OperatorMetadataKey operatorKey, DBType dbType) {
        if (operatorClass == null) {
            throw new IllegalArgumentException("operator class isnull");
        }
        OperatorMetadata operatorMetadata = getOperatorMetadata(operatorKey);
        if (SQLCreateTableOperator.class.isAssignableFrom(operatorClass)) {
            return (T) operatorMetadata.createTable(dbType);
        } else if (SQLDropTableOperator.class.isAssignableFrom(operatorClass)) {
            return (T) operatorMetadata.dropTable(dbType);
        } else if (SQLQueryOperator.class.isAssignableFrom(operatorClass)) {
            return (T) operatorMetadata.query(dbType);
        } else if (SQLInsertOperator.class.isAssignableFrom(operatorClass)) {
            return (T) operatorMetadata.insert(dbType);
        } else if (SQLUpdateOperator.class.isAssignableFrom(operatorClass)) {
            return (T) operatorMetadata.update(dbType);
        } else if (SQLDeleteOperator.class.isAssignableFrom(operatorClass)) {
            return (T) operatorMetadata.delete(dbType);
        } else if (SQLExistTableOperator.class.isAssignableFrom(operatorClass)) {
            return (T) operatorMetadata.existTable(dbType);
        } else if (SQLShowColumnsOperator.class.isAssignableFrom(operatorClass)) {
            return (T) operatorMetadata.showColumns(dbType);
        }
        throw new NullPointerException(String.format("operator class %s system not exist", operatorClass.getName()));
    }
}
