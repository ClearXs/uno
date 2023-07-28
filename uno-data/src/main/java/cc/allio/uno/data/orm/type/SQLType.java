package cc.allio.uno.data.orm.type;

import cc.allio.uno.data.orm.type.db.MySQLType;
import cc.allio.uno.data.orm.type.db.PostgresSQLType;

import java.util.List;

/**
 * SQL 类型定义
 *
 * @author jiangwei
 * @date 2023/4/12 20:03
 * @see DataType
 * @see GenericSQLType
 * @see PostgresSQLType
 * @see MySQLType
 * @since 1.1.4
 */
public interface SQLType {

    /**
     * 获取sql type name
     *
     * @return String
     */
    String getName();

    /**
     * 获取 sql type对应的jdbc type code
     *
     * @return jdbc code
     */
    int getJdbcType();

    /**
     * 获取默认的Precision
     *
     * @return Precision
     */
    Integer getDefaultPrecision();

    /**
     * 获取默认的Scala
     *
     * @return Scala
     */
    Integer getDefaultScala();

    /**
     * 根据db类型创建sqlType
     *
     * @param sqlType sqlType
     * @return SQLType
     */
    static SQLType create(GenericSQLType sqlType) {
        return create(sqlType, DBType.getSystemDbType());
    }

    /**
     * 根据db类型创建sqlType
     *
     * @param sqlType sqlType
     * @param dbType  dbType
     * @return SQLType
     */
    static SQLType create(GenericSQLType sqlType, DBType dbType) {
        if (DBType.POSTGRESQL.equals(dbType)) {
            return new PostgresSQLType(sqlType);
        } else if (DBType.MYSQL.equals(dbType)) {
            return new MySQLType(sqlType);
        }
        return sqlType;
    }

    /**
     * 关联于某一个SQLType
     */
    interface SQLLinkType extends SQLType {

        /**
         * 关联的SQL Type
         *
         * @return SQLType
         */
        List<SQLType> getParent();
    }
}
