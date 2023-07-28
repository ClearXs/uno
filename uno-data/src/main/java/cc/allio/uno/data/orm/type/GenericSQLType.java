package cc.allio.uno.data.orm.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Types;

/**
 * 系统存在的sql type 常量定义
 *
 * @author jiangwei
 * @date 2023/4/18 11:11
 * @since 1.1.4
 */
@Getter
@AllArgsConstructor
public enum GenericSQLType implements SQLType {

    // ====================== 数字型 ======================
    BIGINT("bigint", Types.BIGINT, 64, null),
    SMALLINT("smallint", Types.SMALLINT, 32, null),
    INTEGER("int", Types.INTEGER, 64, null),
    BIT("bit", Types.BIT, 4, null),
    TINYINT("tinyint", Types.TINYINT, 16, null),
    NUMBER("number", Types.NUMERIC, 12, 2),
    DOUBLE("double", Types.DOUBLE, 12, 2),
    FLOAT("float", Types.FLOAT, 12, 2),

    // ====================== 时间型 ======================
    TIME("time", Types.TIME, 6, null),
    TIMESTAMP("timestamp", Types.TIMESTAMP, null, null),
    DATE("date", Types.DATE, 6, null),
    DECIMAL("DECIMAL", Types.DECIMAL, 6, null),

    // ====================== 字符型 ======================
    CHAR("char", Types.CHAR, 64, null),
    VARCHAR("varchar", Types.VARCHAR, 64, null),
    NVARCHAR("nvarchar", Types.NVARCHAR, 64, null),
    LONGVARCHAR("longvarchar", Types.LONGVARCHAR, 1024, null),
    LONGNVARCHAR("longnvarchar", Types.LONGNVARCHAR, 1024, null),
    VARBINARY("varbinary", Types.VARBINARY, 1024, null),
    LONGVARBINARY("longvarchar", Types.LONGVARBINARY, 2048, null),

    // ====================== 高级类型 ======================
    OBJECT("object", Types.JAVA_OBJECT, null, null),
    ARRAY("array", Types.ARRAY, null, null);


    private final String name;
    private final int jdbcType;
    private final Integer defaultPrecision;
    private final Integer defaultScala;

    /**
     * 根据jdbc code获取SQLType实例
     *
     * @param jdbcCode jdbcCode
     * @return SQLType
     */
    public static GenericSQLType getByJdbcCode(int jdbcCode) {
        for (GenericSQLType value : values()) {
            if (value.getJdbcType() == jdbcCode) {
                return value;
            }
        }
        return null;
    }
}
