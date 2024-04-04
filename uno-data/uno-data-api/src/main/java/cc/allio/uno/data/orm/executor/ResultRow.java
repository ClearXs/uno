package cc.allio.uno.data.orm.executor;

import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.type.JavaType;
import cc.allio.uno.data.orm.executor.handler.BoolResultHandler;
import lombok.Builder;
import lombok.Data;

import java.sql.JDBCType;

/**
 * 执行器执行结果
 *
 * @author j.x
 * @date 2023/4/14 17:39
 * @since 1.1.4
 */
@Data
@Builder
public class ResultRow {
    // 结果集索引
    private int index;
    // 字段名称
    private DSLName column;
    // 字段值
    private Object value;
    // jdbc类型
    private JDBCType jdbcType;
    // java类型
    private JavaType<?> javaType;


    /**
     * build the update result value
     *
     * @param value the bool value
     * @return a {@link ResultRow} instance
     */
    public static ResultRow buildUpdateRow(boolean value) {
        ResultRowBuilder builder = ResultRow.builder();
        builder.column(BoolResultHandler.GUESS_UPDATE_OR_UPDATE);
        builder.value(value);
        return builder.build();
    }

    /**
     * build the count result value
     *
     * @param count the count nums value
     * @return a {@link ResultRow} instance
     */
    public static ResultRow buildCountRow(long count) {
        ResultRowBuilder builder = ResultRow.builder();
        builder.column(BoolResultHandler.GUESS_COUNT);
        builder.value(count);
        return builder.build();
    }
}
