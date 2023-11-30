package cc.allio.uno.data.orm.sql.dml;


import cc.allio.uno.data.orm.sql.ColumnStatement;
import cc.allio.uno.core.function.MethodReferenceColumn;

import java.util.Collection;

/**
 * SQL Group相关语法定义
 *
 * @author jiangwei
 * @date 2022/9/30 13:37
 * @since 1.1.0
 * @deprecated 1.1.4版本删除
 */
@Deprecated
public interface Group<T extends Group<T>> extends ColumnStatement<T> {

    /**
     * 由某一个字段进行分组
     *
     * @param reference 方法引用
     * @return Group对象
     */
    default <R> T byOne(MethodReferenceColumn<R> reference) {
        return byOnes(reference.getColumn());
    }

    /**
     * 由某一个字段进行分组
     *
     * @param fieldName java variable name
     * @return Group对象
     */
    T byOne(String fieldName);

    /**
     * 由某一类字段进行分组
     *
     * @param fieldNames java variable name数组
     * @return Group对象
     */
    T byOnes(String... fieldNames);

    /**
     * 由某一类字段进行分组
     *
     * @param fieldNames java variable name集合
     * @return Group对象
     */
    T byOnes(Collection<String> fieldNames);

}
