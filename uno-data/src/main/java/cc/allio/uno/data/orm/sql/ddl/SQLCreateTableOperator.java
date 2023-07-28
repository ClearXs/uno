package cc.allio.uno.data.orm.sql.ddl;

import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.data.orm.sql.PojoWrapper;
import cc.allio.uno.data.orm.sql.SQLColumnDef;
import cc.allio.uno.data.orm.sql.SQLOperator;
import cc.allio.uno.data.orm.sql.SQLTableOperator;

import java.util.List;
import java.util.function.Function;

/**
 * SQL创建操作
 *
 * @author jiangwei
 * @date 2023/4/12 19:42
 * @since 1.1.4
 */
public interface SQLCreateTableOperator extends SQLOperator<SQLCreateTableOperator>, SQLTableOperator<SQLCreateTableOperator> {

    /**
     * 根据pojo的class文件创建SQL创表实例
     *
     * @param pojoClass the pojoClass
     * @return SQLCreateTableOperator
     */
    default <T> SQLCreateTableOperator fromPojo(Class<T> pojoClass) {
        PojoWrapper<T> pojoWrapper = new PojoWrapper<>(pojoClass);
        from(pojoWrapper.getTable());
        List<SQLColumnDef> sqlColumnDefs = pojoWrapper.getSQLColumnDef();
        columns(sqlColumnDefs);
        return self();
    }

    /**
     * 字段
     *
     * @param sqlColumnDefs 集合
     * @return SQLCreateOperator
     */
    default SQLCreateTableOperator columns(SQLColumnDef... sqlColumnDefs) {
        if (sqlColumnDefs != null) {
            for (SQLColumnDef sqlColumnDef : sqlColumnDefs) {
                column(sqlColumnDef);
            }
        }
        return self();
    }

    /**
     * 字段
     *
     * @param sqlColumnDefs 集合
     * @return SQLCreateOperator
     */
    default SQLCreateTableOperator columns(List<SQLColumnDef> sqlColumnDefs) {
        if (CollectionUtils.isNotEmpty(sqlColumnDefs)) {
            for (SQLColumnDef sqlColumnDef : sqlColumnDefs) {
                column(sqlColumnDef);
            }
        }
        return self();
    }

    /**
     * 字段
     *
     * @param builder the builder
     * @return SQLCreateOperator
     */
    default SQLCreateTableOperator column(Function<SQLColumnDef.SQLColumnDefBuilder, SQLColumnDef.SQLColumnDefBuilder> builder) {
        return column(builder.apply(SQLColumnDef.builder()).build());
    }

    /**
     * 字段
     *
     * @param columnDef SQLColumnDef
     * @return SQLCreateOperator
     */
    SQLCreateTableOperator column(SQLColumnDef columnDef);

    /**
     * 约束
     *
     * @param schemaName schemaName
     * @return SQLCreateOperator
     */
    SQLCreateTableOperator schemaName(String schemaName);

    /**
     * 注释
     *
     * @param comment 注释
     * @return SQLCreateOperator
     */
    SQLCreateTableOperator comment(String comment);
}
