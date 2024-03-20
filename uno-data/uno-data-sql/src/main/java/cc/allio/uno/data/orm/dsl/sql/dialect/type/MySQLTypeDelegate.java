package cc.allio.uno.data.orm.dsl.sql.dialect.type;

import cc.allio.uno.data.orm.dsl.type.DSLType;

/**
 * 对mysql库字段进行处理
 *
 * @author j.x
 * @date 2023/4/18 11:15
 * @since 1.1.4
 */
public class MySQLTypeDelegate implements DSLType {

    private final DSLType sqlType;

    public MySQLTypeDelegate(DSLType sqlType) {
        this(sqlType, null, null);
    }

    public MySQLTypeDelegate(DSLType sqlType, Integer precision, Integer scale) {
        this.sqlType = DSLType.create(sqlType, precision, scale);
    }

    @Override
    public String getName() {
        return sqlType.getName();
    }

    @Override
    public int getJdbcType() {
        return sqlType.getJdbcType();
    }

    @Override
    public Integer getPrecision() {
        return sqlType.getPrecision();
    }

    @Override
    public Integer getScale() {
        return sqlType.getScale();
    }
}
