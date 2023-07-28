package cc.allio.uno.data.orm.type.db;

import cc.allio.uno.data.orm.type.GenericSQLType;
import cc.allio.uno.data.orm.type.SQLType;
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
public class PostgresSQLType implements SQLType {

    private SQLType sqlType;

    public PostgresSQLType(GenericSQLType sqlType) {
        for (PostgreSQLLinkType linkType : PostgreSQLLinkType.values()) {
            List<SQLType> parent = linkType.getParent();
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
    public enum PostgreSQLLinkType implements SQLLinkType {
        INT8("int8", Types.BIGINT, null, null, Lists.newArrayList(GenericSQLType.BIGINT)),
        FLOAT("float8", Types.FLOAT, 12, 2, Lists.newArrayList(GenericSQLType.FLOAT)),
        NUMERIC("numeric", Types.DOUBLE, 12, 2, Lists.newArrayList(GenericSQLType.DOUBLE, GenericSQLType.NUMBER)),
        INT4("int4", Types.INTEGER, null, null, Lists.newArrayList(GenericSQLType.INTEGER));

        private final String name;
        private final int jdbcType;
        private final Integer defaultPrecision;
        private final Integer defaultScala;
        private final List<SQLType> parent;
    }
}
