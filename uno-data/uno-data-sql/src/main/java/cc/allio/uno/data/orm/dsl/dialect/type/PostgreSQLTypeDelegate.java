package cc.allio.uno.data.orm.dsl.dialect.type;

import cc.allio.uno.data.orm.dsl.type.DSLType;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Types;
import java.util.List;

/**
 * 对pg库的字段类型进行处理
 *
 * @author jiangwei
 * @date 2023/4/18 11:14
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
        INT8("int8", Types.BIGINT, null, null, Lists.newArrayList(DSLType.BIGINT)),
        FLOAT("float8", Types.FLOAT, 12, 2, Lists.newArrayList(DSLType.FLOAT)),
        NUMERIC("numeric", Types.DOUBLE, 12, 2, Lists.newArrayList(DSLType.DOUBLE, DSLType.NUMBER)),
        INT4("int4", Types.INTEGER, null, null, Lists.newArrayList(DSLType.INTEGER)),
        INT2("int2", Types.INTEGER, null, null, Lists.newArrayList(DSLType.SMALLINT));

        private final String name;
        private final int jdbcType;
        private final Integer precision;
        private final Integer scale;
        private final List<DSLType> parent;
    }
}
