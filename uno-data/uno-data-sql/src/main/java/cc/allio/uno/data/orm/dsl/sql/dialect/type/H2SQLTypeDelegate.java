package cc.allio.uno.data.orm.dsl.sql.dialect.type;

import cc.allio.uno.data.orm.dsl.type.DSLType;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Types;
import java.util.List;

/**
 * h2数据库字段类型二次转换
 *
 * @author j.x
 * @date 2024/1/8 19:42
 * @since 1.1.7
 */
public class H2SQLTypeDelegate extends DSLTypeDelegate {

    public H2SQLTypeDelegate(DSLType sqlType) {
        super(sqlType);
    }

    public H2SQLTypeDelegate(DSLType sqlType, Integer precision, Integer scale) {
        super(sqlType, precision, scale);
    }

    @Override
    protected DSLLinkType[] getDSLLinkValues() {
        return H2LinkType.values();
    }

    @Getter
    @AllArgsConstructor
    public enum H2LinkType implements DSLLinkType {
        H2_BIGINT("bigint", Types.BIGINT, null, null, Lists.newArrayList(BIGINT)),
        H2_INT("int", Types.INTEGER, null, null, Lists.newArrayList(INTEGER)),
        H2_SMALLINT("smallint", Types.SMALLINT, null, null, Lists.newArrayList(SMALLINT)),
        H2_TINYINT("tinyint", Types.SMALLINT, null, null, Lists.newArrayList(TINYINT)),
        NUMERIC("numeric", Types.DOUBLE, 12, 2, Lists.newArrayList(DOUBLE, NUMBER));

        private final String name;
        private final int jdbcType;
        private final Integer precision;
        private final Integer scale;
        private final List<DSLType> parent;
    }
}
