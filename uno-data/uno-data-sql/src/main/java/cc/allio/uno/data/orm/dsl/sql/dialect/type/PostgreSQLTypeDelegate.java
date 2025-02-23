package cc.allio.uno.data.orm.dsl.sql.dialect.type;

import cc.allio.uno.data.orm.dsl.type.DSLType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Types;
import java.util.List;

/**
 * 对pg库的字段类型进行处理
 *
 * @author j.x
 * @since 1.1.4
 */
public class PostgreSQLTypeDelegate extends DSLTypeDelegate {

    static List<DSLType> INT8_OF_LIST = List.of(DSLType.BIGINT);
    static List<DSLType> FLOAT8_OF_LIST = List.of(DSLType.FLOAT);
    static List<DSLType> NUMERIC_OF_LIST = List.of(DSLType.DOUBLE, DSLType.NUMBER);
    static List<DSLType> INT4_OF_LIST = List.of(DSLType.INTEGER);
    static List<DSLType> INT2_OF_LIST = List.of(DSLType.SMALLINT);
    static List<DSLType> TEXT_OF_LIST = List.of(DSLType.LONGVARCHAR);
    static List<DSLType> BOOL_OF_LIST = List.of(DSLType.BOOLEAN);

    public PostgreSQLTypeDelegate(DSLType sqlType) {
        super(sqlType);
    }

    public PostgreSQLTypeDelegate(DSLType sqlType, Integer precision, Integer scale) {
        super(sqlType, precision, scale);
    }

    @Override
    protected DSLLinkType[] getDSLLinkValues() {
        return PostgreSQLLinkType.values();
    }

    @Getter
    @AllArgsConstructor
    public enum PostgreSQLLinkType implements DSLLinkType {


        INT8("int8", Types.BIGINT, null, null, INT8_OF_LIST),
        FLOAT("float8", Types.FLOAT, 12, 2, FLOAT8_OF_LIST),
        NUMERIC("numeric", Types.DOUBLE, 12, 2, NUMERIC_OF_LIST),
        INT4("int4", Types.INTEGER, null, null, INT4_OF_LIST),
        INT2("int2", Types.INTEGER, null, null, INT2_OF_LIST),
        TEXT("text", Types.LONGVARCHAR, null, null, TEXT_OF_LIST),
        BOOL("bool", Types.BOOLEAN, null, null, BOOL_OF_LIST);

        private final String name;
        private final int jdbcType;
        private final Integer precision;
        private final Integer scale;
        private final List<DSLType> parent;

        @Override
        public boolean equalsTo(DSLType other) {
            return parent.contains(other);
        }
    }
}
