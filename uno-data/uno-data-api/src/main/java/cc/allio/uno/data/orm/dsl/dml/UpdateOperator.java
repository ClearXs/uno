package cc.allio.uno.data.orm.dsl.dml;

import cc.allio.uno.core.function.MethodReferenceColumn;
import cc.allio.uno.data.orm.dsl.*;
import com.google.common.collect.Maps;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * SQL Update Operator
 *
 * @author jiangwei
 * @date 2023/4/16 15:19
 * @since 1.1.4
 */
public interface UpdateOperator extends PrepareOperator<UpdateOperator>, TableOperator<UpdateOperator>, WhereOperator<UpdateOperator> {

    /**
     * UPDATE VALUES
     *
     * @param reference key
     * @param value     value
     * @return SQLUpdateOperator
     */
    default <R> UpdateOperator update(MethodReferenceColumn<R> reference, Object value) {
        return update(reference.getColumn(), getValueIfNullThenNullValue(value));
    }

    /**
     * UPDATE VALUES
     *
     * @param fieldName fieldName
     * @param value     value
     * @return SQLUpdateOperator
     */
    default UpdateOperator update(String fieldName, Object value) {
        return update(Tuples.of(fieldName, getValueIfNullThenNullValue(value)));
    }

    /**
     * UPDATE VALUES
     *
     * @param f1 f1
     * @param v1 v1
     * @return SQLUpdateOperator
     */
    default UpdateOperator update(String f1, Object v1, String f2, Object v2) {
        return update(Tuples.of(f1, getValueIfNullThenNullValue(v1)), Tuples.of(f2, getValueIfNullThenNullValue(v2)));
    }

    /**
     * UPDATE VALUES
     *
     * @param f1 f1
     * @param v1 v1
     * @param f2 f2
     * @param v2 v2
     * @return SQLUpdateOperator
     */
    default UpdateOperator update(String f1, Object v1, String f2, Object v2, String f3, Object v3) {
        return update(
                Tuples.of(f1, getValueIfNullThenNullValue(v1)),
                Tuples.of(f2, getValueIfNullThenNullValue(v2)),
                Tuples.of(f3, getValueIfNullThenNullValue(v3)));
    }

    /**
     * UPDATE VALUES
     *
     * @param f1 f1
     * @param v1 v1
     * @param f2 f2
     * @param v2 v2
     * @param f3 f3
     * @param v3 v3
     * @param f4 f4
     * @param v4 v4
     * @return SQLUpdateOperator
     */
    default UpdateOperator update(String f1, Object v1, String f2, Object v2, String f3, Object v3, String f4, Object v4) {
        return update(
                Tuples.of(f1, getValueIfNullThenNullValue(v1)),
                Tuples.of(f2, getValueIfNullThenNullValue(v2)),
                Tuples.of(f3, getValueIfNullThenNullValue(v3)),
                Tuples.of(f4, getValueIfNullThenNullValue(v4)));
    }

    /**
     * UPDATE VALUES
     *
     * @param tuple2s Key value
     * @return SQLUpdateOperator
     */
    default UpdateOperator update(Tuple2<String, Object>... tuple2s) {
        Map<String, Object> values = Maps.newHashMap();
        for (Tuple2<String, Object> tuple2 : tuple2s) {
            values.put(tuple2.getT1(), tuple2.getT2());
        }
        return update(values);
    }

    /**
     * UPDATE VALUES
     *
     * @param pojo pojo
     * @return SQLUpdateOperator
     */
    default UpdateOperator updatePojo(Object pojo) {
        PojoWrapper<Object> pojoWrapper = new PojoWrapper<>(pojo);
        List<ColumnDef> notPkColumns = pojoWrapper.getNotPkColumns();
        Map<DSLName, Object> values = Maps.newLinkedHashMap();
        for (ColumnDef notPkColumn : notPkColumns) {
            Object value = pojoWrapper.getValueByColumn(notPkColumn);
            values.put(notPkColumn.getDslName(), value);
        }
        return from(pojoWrapper.getTable()).updates(values);
    }

    /**
     * UPDATE VALUES，将会过滤为空的值
     *
     * @param values Key value
     * @return SQLUpdateOperator
     */
    default UpdateOperator update(Map<String, Object> values) {
        Map<DSLName, Object> updates = Maps.newLinkedHashMap();
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            updates.put(DSLName.of(entry.getKey()), entry.getValue());
        }
        return updates(updates);
    }

    /**
     * UPDATE VALUES
     *
     * @param values Key value
     * @return SQLUpdateOperator
     */
    UpdateOperator updates(Map<DSLName, Object> values);

    /**
     * @see #strictFill(String, Supplier)
     */
    default UpdateOperator strictFill(String f, Object v) {
        return strictFill(f, () -> v);
    }

    /**
     * 当使用了{@link #updates(Map)}等API时，
     * 如果需要对某一个或多个字段进行其他方式的设置值，此时需要调用该API进行重新填充
     *
     * @param f 字段名
     * @param v 字段值
     * @return SQLInsertOperator
     */
    UpdateOperator strictFill(String f, Supplier<Object> v);
}
