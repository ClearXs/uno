package cc.allio.uno.data.orm.dsl.ddl;

import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;

/**
 * Show Tables Operator
 *
 * @author j.x
 * @date 2024/1/4 16:56
 * @since 1.1.7
 * @see OperatorGroup
 */
public interface ShowTablesOperator
        extends TableOperator<ShowTablesOperator>,
        PrepareOperator<ShowTablesOperator>, DataBaseOperator<ShowTablesOperator> {

    String TABLE_CATALOG_FILED = "TABLE_CATALOG";
    String TABLE_SCHEMA_FILED = "TABLE_SCHEMA";
    String TABLE_NAME_FILED = "TABLE_NAME";
    String TABLE_TYPE_FILED = "TABLE_TYPE";

    /**
     * 转换为{@link QueryOperator}
     *
     * @return SQLQueryOperator for instance
     */
    QueryOperator toQueryOperator();

    /**
     * schema
     *
     * @param schema schema
     * @return ShowTablesOperator
     */
    ShowTablesOperator schema(String schema);

    /**
     * 作为库表查询的过滤，可以多次使用，如果多次则查询这多个信息
     *
     * @param table xxxx
     * @return ShowTablesOperator
     */
    @Override
    ShowTablesOperator from(Table table);
}
