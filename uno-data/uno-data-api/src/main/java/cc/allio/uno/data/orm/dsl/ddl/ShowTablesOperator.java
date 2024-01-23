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
public interface ShowTablesOperator extends PrepareOperator<ShowTablesOperator>, Operator<ShowTablesOperator>, Self<ShowTablesOperator> {

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
     * @return SQLShowTablesOperator
     */
    default ShowTablesOperator database(String database) {
        return database(Database.of(DSLName.of(database)));
    }

    /**
     * database
     *
     * @param database database
     * @return SQLShowTablesOperator
     */
    ShowTablesOperator database(Database database);

    /**
     * schema
     *
     * @param schema schema
     * @return SQLShowTablesOperator
     */
    ShowTablesOperator schema(String schema);
}
