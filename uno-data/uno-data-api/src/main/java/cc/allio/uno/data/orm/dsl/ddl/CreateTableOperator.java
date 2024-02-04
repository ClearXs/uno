package cc.allio.uno.data.orm.dsl.ddl;

import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.data.orm.dsl.PojoWrapper;
import cc.allio.uno.data.orm.dsl.ColumnDef;
import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.TableOperator;

import java.util.List;
import java.util.function.UnaryOperator;

/**
 * SQL创建操作
 *
 * @author jiangwei
 * @date 2023/4/12 19:42
 * @since 1.1.4
 */
public interface CreateTableOperator extends Operator<CreateTableOperator>, TableOperator<CreateTableOperator> {

    /**
     * 根据pojo的clas创表实例
     *
     * @param pojoClass the pojoClass
     * @return SQLCreateTableOperator
     */
    default <T> CreateTableOperator fromPojo(Class<T> pojoClass) {
        PojoWrapper<T> pojoWrapper = new PojoWrapper<>(pojoClass);
        List<ColumnDef> columnDefs = pojoWrapper.getColumnDef();
        return from(pojoWrapper.getTable()).columns(columnDefs);
    }

    /**
     * 字段
     *
     * @param columnDefs 集合
     * @return CreateTableOperator
     */
    default CreateTableOperator columns(ColumnDef... columnDefs) {
        if (columnDefs != null) {
            for (ColumnDef columnDef : columnDefs) {
                column(columnDef);
            }
        }
        return self();
    }

    /**
     * 字段
     *
     * @param columnDefs 集合
     * @return CreateTableOperator
     */
    default CreateTableOperator columns(List<ColumnDef> columnDefs) {
        if (CollectionUtils.isNotEmpty(columnDefs)) {
            for (ColumnDef columnDef : columnDefs) {
                column(columnDef);
            }
        }
        return self();
    }

    /**
     * 字段
     *
     * @param builder the builder
     * @return CreateTableOperator
     */
    default CreateTableOperator column(UnaryOperator<ColumnDef.ColumnDefBuilder> builder) {
        return column(builder.apply(ColumnDef.builder()).build());
    }

    /**
     * 字段
     *
     * @param columnDef SQLColumnDef
     * @return CreateTableOperator
     */
    CreateTableOperator column(ColumnDef columnDef);

    /**
     * 注释
     *
     * @param comment 注释
     * @return CreateTableOperator
     */
    CreateTableOperator comment(String comment);
}
