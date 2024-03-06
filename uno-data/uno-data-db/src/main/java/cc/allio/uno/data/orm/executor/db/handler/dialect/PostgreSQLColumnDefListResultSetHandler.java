package cc.allio.uno.data.orm.executor.db.handler.dialect;

import cc.allio.uno.data.orm.dsl.ColumnDef;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.ddl.ShowColumnsOperator;
import cc.allio.uno.data.orm.dsl.dialect.TypeTranslator;
import cc.allio.uno.data.orm.dsl.dialect.TypeTranslatorHolder;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.dsl.type.DataType;
import cc.allio.uno.data.orm.executor.ResultSet;
import cc.allio.uno.data.orm.executor.handler.ColumnDefListResultSetHandler;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Optional;

/**
 * {@link cc.allio.uno.data.orm.dsl.type.DBType#POSTGRESQL}的{@link cc.allio.uno.data.orm.executor.CommandExecutor#showColumns(Table)}结果集处理器
 *
 * @author jiangwei
 * @date 2024/2/14 23:19
 * @since 1.1.7
 */
public class PostgreSQLColumnDefListResultSetHandler extends ColumnDefListResultSetHandler {

    /**
     * pg 数据类型名称字段
     */
    private static final String UDT_NAME = "UDT_NAME";

    @Override
    public List<ColumnDef> apply(ResultSet resultSet) {
        return Lists.newArrayList(resultSet)
                .stream()
                .map(r -> {
                    ColumnDef.ColumnDefBuilder columnDefBuilder = commonFieldBuilder(r);
                    DBType dbType = obtainExecutorOptions().getDbType();
                    TypeTranslator typeTranslator = TypeTranslatorHolder.getTypeTranslator(dbType);
                    DataType dataType = Optional.ofNullable(typeTranslator.reserve(r.getStringValue(ShowColumnsOperator.DATA_TYPE_FIELD)))
                            .or(() -> Optional.ofNullable(typeTranslator.reserve(r.getStringValue(UDT_NAME))))
                            .map(d -> buildDataType(d, r))
                            .orElse(null);
                    columnDefBuilder.dataType(dataType);
                    return columnDefBuilder.build();
                })
                .toList();
    }
}
