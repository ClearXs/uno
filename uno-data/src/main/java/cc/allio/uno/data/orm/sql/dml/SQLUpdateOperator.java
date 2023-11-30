package cc.allio.uno.data.orm.sql.dml;

import cc.allio.uno.core.bean.ObjectWrapper;
import cc.allio.uno.core.function.MethodReferenceColumn;
import cc.allio.uno.data.orm.sql.*;
import com.google.common.collect.Maps;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.Map;

/**
 * SQL Update Operator
 *
 * @author jiangwei
 * @date 2023/4/16 15:19
 * @since 1.1.4
 */
public interface SQLUpdateOperator extends SQLPrepareOperator<SQLUpdateOperator>, SQLTableOperator<SQLUpdateOperator>, SQLWhereOperator<SQLUpdateOperator> {

    /**
     * UPDATE VALUES
     *
     * @param reference key
     * @param value     value
     * @return SQLUpdateOperator
     */
    default <R> SQLUpdateOperator update(MethodReferenceColumn<R> reference, Object value) {
        return update(reference.getColumn(), value);
    }

    /**
     * UPDATE VALUES
     *
     * @param fieldName fieldName
     * @param value     value
     * @return SQLUpdateOperator
     */
    default SQLUpdateOperator update(String fieldName, Object value) {
        return update(Tuples.of(fieldName, value));
    }

    /**
     * UPDATE VALUES
     *
     * @param f1 f1
     * @param v1 v1
     * @return SQLUpdateOperator
     */
    default SQLUpdateOperator update(String f1, Object v1, String f2, Object v2) {
        return update(Tuples.of(f1, v1), Tuples.of(f2, v2));
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
    default SQLUpdateOperator update(String f1, Object v1, String f2, Object v2, String f3, Object v3) {
        return update(Tuples.of(f1, v1), Tuples.of(f2, v2), Tuples.of(f3, v3));
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
    default SQLUpdateOperator update(String f1, Object v1, String f2, Object v2, String f3, Object v3, String f4, Object v4) {
        return update(Tuples.of(f1, v1), Tuples.of(f2, v2), Tuples.of(f3, v3), Tuples.of(f4, v4));
    }

    /**
     * UPDATE VALUES
     *
     * @param tuple2s Key value
     * @return SQLUpdateOperator
     */
    default SQLUpdateOperator update(Tuple2<String, Object>... tuple2s) {
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
    default SQLUpdateOperator updatePojo(Object pojo) {
        ObjectWrapper wrapper = new ObjectWrapper(pojo);
        return update(wrapper.findAllValuesForce());
    }

    /**
     * UPDATE VALUES，将会过滤为空的值
     *
     * @param values Key value
     * @return SQLUpdateOperator
     */
    default SQLUpdateOperator update(Map<String, Object> values) {
        Map<SQLName, Object> updates = Maps.newHashMap();
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            Object value = entry.getValue();
            if (value != null) {
                updates.put(SQLName.of(entry.getKey()), entry.getValue());
            }
        }
        return updates(updates);
    }

    /**
     * UPDATE VALUES
     *
     * @param values Key value
     * @return SQLUpdateOperator
     */
    SQLUpdateOperator updates(Map<SQLName, Object> values);
}
