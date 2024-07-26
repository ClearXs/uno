package cc.allio.uno.data.orm.dsl.ddl;

import cc.allio.uno.data.orm.dsl.DataBaseOperator;
import cc.allio.uno.data.orm.dsl.opeartorgroup.Operators;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.orm.dsl.PrepareOperator;
import cc.allio.uno.data.orm.dsl.TableOperator;

/**
 * Show Columns
 *
 * @author j.x
 * @date 2023/6/8 19:19
 * @see Operators
 * @since 1.1.4
 */
public interface ShowColumnsOperator<T extends ShowColumnsOperator<T>> extends PrepareOperator<T>, TableOperator<T>, DataBaseOperator<T> {

    String TABLE_CATALOG_FIELD = "TABLE_CATALOG";
    String TABLE_SCHEMA_FILED = "TABLE_SCHEMA";
    String TABLE_NAME_FILED = "TABLE_NAME";
    String COLUMN_NAME_FIELD = "COLUMN_NAME";
    String COLUMN_COMMENT_FIELD = "COLUMN_COMMENT";
    String ORDINAL_POSITION_FIELD = "ORDINAL_POSITION";
    String COLUMN_DEFAULT_FIELD = "COLUMN_DEFAULT";
    String IS_NULLABLE_FIELD = "IS_NULLABLE";
    String DATA_TYPE_FIELD = "DATA_TYPE";
    String CHARACTER_MAXIMUM_LENGTH_FIELD = "CHARACTER_MAXIMUM_LENGTH";
    String CHARACTER_OCTET_LENGTH_FIELD = "CHARACTER_OCTET_LENGTH";
    String NUMERIC_PRECISION_FIELD = "NUMERIC_PRECISION";
    String NUMERIC_SCALE_FIELD = "NUMERIC_SCALE";
    String DATETIME_PRECISION_FIELD = "DATETIME_PRECISION";

    /**
     * 转换为{@link QueryOperator}
     *
     * @return {@link QueryOperator} for instance
     */
    QueryOperator<?> toQueryOperator();
}
