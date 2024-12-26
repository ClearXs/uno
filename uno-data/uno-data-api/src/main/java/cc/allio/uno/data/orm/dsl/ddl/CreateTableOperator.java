package cc.allio.uno.data.orm.dsl.ddl;

import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.data.orm.dsl.opeartorgroup.Operators;
import cc.allio.uno.data.orm.dsl.helper.PojoWrapper;
import cc.allio.uno.data.orm.dsl.ColumnDef;
import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.TableOperator;

import java.util.List;
import java.util.function.UnaryOperator;

/**
 * Create Operator
 *
 * @author j.x
 * @see Operators
 * @since 1.1.4
 */
public interface CreateTableOperator<T extends CreateTableOperator<T>> extends Operator<T>, TableOperator<T> {

    /**
     * 根据pojo的clas创表实例
     *
     * @param pojoClass the pojoClass
     * @return self
     */
    default <P> T fromPojo(Class<P> pojoClass) {
        PojoWrapper<P> pojoWrapper = PojoWrapper.getInstance(pojoClass);
        List<ColumnDef> columnDefs = pojoWrapper.getColumnDefs();
        return from(pojoWrapper.getTable()).columns(columnDefs);
    }

    /**
     * 字段
     *
     * @param columnDefs 集合
     * @return self
     */
    default T columns(ColumnDef... columnDefs) {
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
     * @return self
     */
    default T columns(List<ColumnDef> columnDefs) {
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
     * @return self
     */
    default T column(UnaryOperator<ColumnDef.ColumnDefBuilder> builder) {
        return column(builder.apply(ColumnDef.builder()).build());
    }

    /**
     * 字段
     *
     * @param columnDef SQLColumnDef
     * @return self
     */
    T column(ColumnDef columnDef);

    /**
     * 注释
     *
     * @param comment 注释
     * @return self
     */
    T comment(String comment);
}
