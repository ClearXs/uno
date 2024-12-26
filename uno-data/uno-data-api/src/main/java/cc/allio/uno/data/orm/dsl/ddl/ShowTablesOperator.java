package cc.allio.uno.data.orm.dsl.ddl;

import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.orm.dsl.opeartorgroup.Operators;

/**
 * Show Tables Operator
 *
 * @author j.x
 * @see Operators
 * @since 1.1.7
 */
public interface ShowTablesOperator<T extends ShowTablesOperator<T>> extends TableOperator<T>, PrepareOperator<T>, DataBaseOperator<T> {

    String TABLE_CATALOG_FILED = "TABLE_CATALOG";
    String TABLE_SCHEMA_FILED = "TABLE_SCHEMA";
    String TABLE_NAME_FILED = "TABLE_NAME";
    String TABLE_TYPE_FILED = "TABLE_TYPE";
    String TABLE_COMMENT_FILED = "TABLE_COMMENT";

    /**
     * 转换为{@link QueryOperator}
     *
     * @return {@link QueryOperator}
     */
    QueryOperator<?> toQueryOperator();

    /**
     * schema
     *
     * @param schema schema
     * @return self
     */
    T schema(String schema);

    /**
     * 作为库表查询的过滤，可以多次使用，如果多次则查询这多个信息
     *
     * @param table xxxx
     * @return self
     */
    @Override
    T from(Table table);
}
