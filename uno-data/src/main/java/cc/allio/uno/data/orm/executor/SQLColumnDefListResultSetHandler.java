package cc.allio.uno.data.orm.executor;

import cc.allio.uno.core.type.Types;
import cc.allio.uno.data.orm.sql.SQLColumnDef;
import cc.allio.uno.data.orm.sql.SQLName;
import cc.allio.uno.data.orm.type.DataType;
import cc.allio.uno.data.orm.type.GenericSQLType;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link SQLColumnDef}结果集处理器
 *
 * @author jiangwei
 * @date 2023/7/4 14:54
 * @since 1.1.4
 */
public class SQLColumnDefListResultSetHandler implements ListResultSetHandler<SQLColumnDef> {

    /**
     * 用于保存字段名称
     */
    public static final String ROW_FIELD_NAME = "field";

    @Override
    public List<SQLColumnDef> apply(ResultSet resultGroups) {
        return Lists.newArrayList(resultGroups)
                .stream()
                .map(r -> {
                    ResultRow field = r.getRow(ROW_FIELD_NAME);
                    return SQLColumnDef.builder()
                            .sqlName(SQLName.of(Types.toString(field.getValue()), SQLName.PLAIN_FEATURE))
                            .dataType(DataType.create(GenericSQLType.getByJdbcCode(field.getJdbcType().getVendorTypeNumber())))
                            .build();
                })
                .collect(Collectors.toList());
    }
}
