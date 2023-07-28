package cc.allio.uno.data.orm.type.db;

import cc.allio.uno.data.orm.type.GenericSQLType;
import cc.allio.uno.data.orm.type.SQLType;

/**
 * 对mysql库字段进行处理
 *
 * @author jiangwei
 * @date 2023/4/18 11:15
 * @since 1.1.4
 */
public class MySQLType implements SQLType {

    private final GenericSQLType sqlType;

    public MySQLType(GenericSQLType sqlType) {
        this.sqlType = sqlType;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public int getJdbcType() {
        return 0;
    }

    @Override
    public Integer getDefaultPrecision() {
        return null;
    }

    @Override
    public Integer getDefaultScala() {
        return null;
    }
}
