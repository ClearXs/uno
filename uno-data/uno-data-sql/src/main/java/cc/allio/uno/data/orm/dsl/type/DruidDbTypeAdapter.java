package cc.allio.uno.data.orm.dsl.type;

import com.alibaba.druid.DbType;

/**
 * DruidDbTypeBridge
 *
 * @author jiangwei
 * @date 2023/2/23 16:47
 * @since 1.1.4
 */
public class DruidDbTypeAdapter implements DBTypeAdapter<DbType> {

    private static final DruidDbTypeAdapter INSTANCE = new DruidDbTypeAdapter();

    public static DruidDbTypeAdapter getInstance() {
        return INSTANCE;
    }

    @Override
    public DbType adapt(DBType o) {
        DBType dbType = o;
        if (DBType.MYSQL.equals(dbType)) {
            return DbType.mysql;
        } else if (DBType.ORACLE.equals(dbType)) {
            return DbType.oracle;
        } else if (DBType.POSTGRESQL.equals(dbType)) {
            return DbType.postgresql;
        } else if (DBType.SQLSERVER.equals(dbType)) {
            return DbType.sqlserver;
        } else if (DBType.H2.equals(dbType)) {
            return DbType.h2;
        }
        return DbType.h2;
    }

    @Override
    public DBType reverse(DbType dbType) {
        return switch (dbType) {
            case DbType.mysql -> DBType.MYSQL;
            case DbType.oracle -> DBType.ORACLE;
            case DbType.postgresql -> DBType.POSTGRESQL;
            case DbType.sqlserver -> DBType.SQLSERVER;
            default -> DBType.H2;
        };
    }
}
