package cc.allio.uno.data.orm.dsl.dialect.type;

import cc.allio.uno.data.orm.dsl.type.DSLType;

/**
 * 对mysql库字段进行处理
 *
 * @author jiangwei
 * @date 2023/4/18 11:15
 * @since 1.1.4
 */
public class MySQLType implements DSLType {

    private final DSLType sqlType;

    public MySQLType(DSLType sqlType) {
        this(sqlType, null, null);
    }

    public MySQLType(DSLType sqlType, Integer precision, Integer scale) {
        this.sqlType = DSLTypeImpl.createBy(sqlType, precision, scale);
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
