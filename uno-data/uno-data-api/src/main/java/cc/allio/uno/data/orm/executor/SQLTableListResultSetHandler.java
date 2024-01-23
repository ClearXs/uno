package cc.allio.uno.data.orm.executor;

import cc.allio.uno.core.type.Types;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.ddl.ShowTablesOperator;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * {@link Table}结果集处理器
 *
 * @author jiangwei
 * @date 2024/1/4 18:56
 * @since 1.1.6
 */
public class SQLTableListResultSetHandler implements ListResultSetHandler<Table> {

    @Override
    public List<Table> apply(ResultSet resultSet) {
        return Lists.newArrayList(resultSet)
                .stream()
                .map(r -> {
                    Table table = new Table();
                    table.setCatalog(Types.toString(r.getRow(ShowTablesOperator.CATALOG_FILED).getValue()));
                    table.setSchema(Types.toString(r.getRow(ShowTablesOperator.SCHEMA_FILED).getValue()));
                    table.setName(DSLName.of(Types.toString(r.getRow(ShowTablesOperator.NAME_FILED).getValue()), DSLName.PLAIN_FEATURE));
                    table.setType(Types.toString(r.getRow(ShowTablesOperator.TYPE_FILED).getValue()));
                    return table;
                })
                .toList();
    }
}
