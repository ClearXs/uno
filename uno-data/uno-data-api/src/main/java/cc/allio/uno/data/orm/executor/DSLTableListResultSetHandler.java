package cc.allio.uno.data.orm.executor;

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
public class DSLTableListResultSetHandler implements ListResultSetHandler<Table> {

    @Override
    public List<Table> apply(ResultSet resultSet) {
        return Lists.newArrayList(resultSet)
                .stream()
                .map(r -> {
                    Table table = new Table();
                    r.getOptionStringValue(ShowTablesOperator.CATALOG_FILED, table::setCatalog);
                    r.getOptionStringValue(ShowTablesOperator.SCHEMA_FILED, table::setSchema);
                    r.getOptionStringValue(ShowTablesOperator.NAME_FILED, name -> table.setName(DSLName.of(name, DSLName.PLAIN_FEATURE)));
                    r.getOptionStringValue(ShowTablesOperator.TYPE_FILED, table::setType);
                    return table;
                })
                .toList();
    }
}
