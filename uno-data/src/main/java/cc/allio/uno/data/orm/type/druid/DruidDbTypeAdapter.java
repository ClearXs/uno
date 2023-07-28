package cc.allio.uno.data.orm.type.druid;

import com.alibaba.druid.DbType;
import cc.allio.uno.data.orm.type.DBType;
import cc.allio.uno.data.orm.type.DBTypeAdapter;

/**
 * DruidDbTypeBridge
 *
 * @author jiangwei
 * @date 2023/2/23 16:47
 * @since 1.1.4
 */
public class DruidDbTypeAdapter implements DBTypeAdapter<DbType> {

    private static final DruidDbTypeAdapter INSTANCE = new DruidDbTypeAdapter();

    @Override
    public DbType get(DBType o) {
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
        return null;
    }

    @Override
    public DBType reversal(DbType dbType) {
        switch (dbType) {
            case mysql:
                return DBType.MYSQL;
            case oracle:
                return DBType.ORACLE;
            case postgresql:
                return DBType.POSTGRESQL;
            case sqlserver:
                return DBType.SQLSERVER;
            case h2:
                return DBType.H2;
        }
        return null;
    }

    public static DruidDbTypeAdapter getInstance() {
        return INSTANCE;
    }
}
