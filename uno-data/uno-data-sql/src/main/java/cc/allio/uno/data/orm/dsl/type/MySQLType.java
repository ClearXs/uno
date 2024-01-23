package cc.allio.uno.data.orm.dsl.type;

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
        this.sqlType = sqlType;
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
    public Integer getDefaultPrecision() {
        return sqlType.getDefaultPrecision();
    }

    @Override
    public Integer getDefaultScala() {
        return sqlType.getDefaultScala();
    }
}
