package cc.allio.uno.data.orm.executor;

import cc.allio.uno.core.type.Types;
import cc.allio.uno.data.orm.dsl.ColumnDef;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.type.DataType;
import cc.allio.uno.data.orm.dsl.type.DSLType;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * {@link ColumnDef}结果集处理器
 *
 * @author jiangwei
 * @date 2023/7/4 14:54
 * @since 1.1.4
 */
public class SQLColumnDefListResultSetHandler implements ListResultSetHandler<ColumnDef> {

    /**
     * 用于保存字段名称
     */
    public static final String ROW_FIELD_NAME = "FIELD";

    @Override
    public List<ColumnDef> apply(ResultSet resultSet) {
        return Lists.newArrayList(resultSet)
                .stream()
                .map(r -> {
                    ResultRow field = r.getRow(ROW_FIELD_NAME);
                    return ColumnDef.builder()
                            .dslName(DSLName.of(Types.toString(field.getValue()), DSLName.PLAIN_FEATURE))
                            .dataType(DataType.create(DSLType.getByJdbcCode(field.getJdbcType().getVendorTypeNumber())))
                            .build();
                })
                .toList();
    }
}
