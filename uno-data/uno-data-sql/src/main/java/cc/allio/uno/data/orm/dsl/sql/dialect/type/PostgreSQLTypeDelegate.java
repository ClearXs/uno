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

        INT8("int8", Types.BIGINT, null, null, List.of(DSLType.BIGINT)),
        FLOAT("float8", Types.FLOAT, 12, 2, List.of(DSLType.FLOAT)),
        NUMERIC("numeric", Types.DOUBLE, 12, 2, List.of(DSLType.DOUBLE, DSLType.NUMBER)),
        INT4("int4", Types.INTEGER, null, null, List.of(DSLType.INTEGER)),
        INT2("int2", Types.INTEGER, null, null, List.of(DSLType.SMALLINT)),
        TEXT("text", Types.LONGVARCHAR, null, null, List.of(DSLType.LONGVARCHAR)),
        BOOL("bool", Types.BOOLEAN, null, null, List.of(DSLType.BOOLEAN));

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
