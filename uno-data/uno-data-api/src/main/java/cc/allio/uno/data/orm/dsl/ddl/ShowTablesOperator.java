package cc.allio.uno.data.orm.dsl.ddl;

import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;

/**
 * 数据库表结构
 *
 * @author jiangwei
 * @date 2024/1/4 16:56
 * @since 1.1.6
 */
public interface ShowTablesOperator extends TableOperator<ShowTablesOperator>, PrepareOperator<ShowTablesOperator>, Self<ShowTablesOperator> {

    String CATALOG_FILED = "CATALOG";
    String SCHEMA_FILED = "SCHEMA";
    String NAME_FILED = "NAME";
    String TYPE_FILED = "TYPE";

    /**
     * 转换为{@link QueryOperator}
     *
     * @return SQLQueryOperator for instance
     */
    QueryOperator toQueryOperator();

    /**
     * database
     *
     * @param database database
     * @return ShowTablesOperator
     */
    default ShowTablesOperator database(String database) {
        return database(Database.of(DSLName.of(database)));
    }

    /**
     * database
     *
     * @param database database
     * @return ShowTablesOperator
     */
    ShowTablesOperator database(Database database);

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
