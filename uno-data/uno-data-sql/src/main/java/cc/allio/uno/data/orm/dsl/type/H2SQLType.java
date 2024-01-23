package cc.allio.uno.data.orm.dsl.type;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Types;
import java.util.List;

/**
 * h2数据库字段类型二次转换
 *
 * @author jiangwei
 * @date 2024/1/8 19:42
 * @since 1.1.6
 */
public class H2SQLType implements DSLType {

    private DSLType sqlType;

    public H2SQLType(DSLType sqlType) {
        for (H2LinkType linkType : H2LinkType.values()) {
            List<DSLType> parent = linkType.getParent();
            if (parent.stream().anyMatch(p -> p.getName().equals(sqlType.getName()))) {
                this.sqlType = linkType;
                break;
            }
        }
        if (this.sqlType == null) {
            this.sqlType = sqlType;
        }
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

    @Getter
    @AllArgsConstructor
    public enum H2LinkType implements DSLLinkType {
        H2_BIGINT("bigint", Types.BIGINT, null, null, Lists.newArrayList(DSLType.BIGINT)),
        H2_INT("int", Types.INTEGER, null, null, Lists.newArrayList(DSLType.INTEGER)),
        H2_SMALLINT("smallint", Types.SMALLINT, null, null, Lists.newArrayList(DSLType.SMALLINT)),
        H2_TINYINT("tinyint", Types.SMALLINT, null, null, Lists.newArrayList(DSLType.TINYINT)),
        NUMERIC("numeric", Types.DOUBLE, 12, 2, Lists.newArrayList(DSLType.DOUBLE, DSLType.NUMBER));

        private final String name;
        private final int jdbcType;
        private final Integer defaultPrecision;
        private final Integer defaultScala;
        private final List<DSLType> parent;
    }
}
