package cc.allio.uno.data.orm.sql.dml;

import cc.allio.uno.core.bean.ObjectWrapper;
import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.data.orm.sql.MethodReferenceColumn;
import cc.allio.uno.data.orm.sql.SQLName;
import cc.allio.uno.data.orm.sql.SQLPrepareOperator;
import cc.allio.uno.data.orm.sql.SQLTableOperator;
import com.google.common.collect.Maps;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * SQL INSERT。值得注意的是没调用一次{@link #insert(String, Object)} }api，都会生成一个VALUES。建议调用{@link #batchInserts(Collection, Collection)}api批量生成
 *
 * @author jiangwei
 * @date 2023/4/13 15:25
 * @since 1.1.4
 */
public interface SQLInsertOperator extends SQLPrepareOperator<SQLInsertOperator>, SQLTableOperator<SQLInsertOperator> {


    /**
     * INSERT VALUES
     *
     * @param reference key
     * @param value     value
     * @return InsertOperator
     */
    default <R> SQLInsertOperator insert(MethodReferenceColumn<R> reference, Object value) {
        return insert(reference.getColumn(), value);
    }

    /**
     * INSERT VALUES
     *
     * @param fieldName fieldName
     * @param value     value
     * @return InsertOperator
     */
    default SQLInsertOperator insert(String fieldName, Object value) {
        return insert(Tuples.of(fieldName, value));
    }

    /**
     * INSERT VALUES
     *
     * @param f1 f1
     * @param v1 v1
     * @return InsertOperator
     */
    default SQLInsertOperator insert(String f1, Object v1, String f2, Object v2) {
        return insert(Tuples.of(f1, v1), Tuples.of(f2, v2));
    }

    /**
     * INSERT VALUES
     *
     * @param f1 f1
     * @param v1 v1
     * @param f2 f2
     * @param v2 v2
     * @return InsertOperator
     */
    default SQLInsertOperator insert(String f1, Object v1, String f2, Object v2, String f3, Object v3) {
        return insert(Tuples.of(f1, v1), Tuples.of(f2, v2), Tuples.of(f3, v3));
    }

    /**
     * INSERT VALUES
     *
     * @param f1 f1
     * @param v1 v1
     * @param f2 f2
     * @param v2 v2
     * @param f3 f3
     * @param v3 v3
     * @param f4 f4
     * @param v4 v4
     * @return InsertOperator
     */
    default SQLInsertOperator insert(String f1, Object v1, String f2, Object v2, String f3, Object v3, String f4, Object v4) {
        return insert(Tuples.of(f1, v1), Tuples.of(f2, v2), Tuples.of(f3, v3), Tuples.of(f4, v4));
    }

    /**
     * INSERT VALUES
     *
     * @param tuple2s Key value
     * @return InsertOperator
     */
    default SQLInsertOperator insert(Tuple2<String, Object>... tuple2s) {
        Map<String, Object> values = Maps.newHashMap();
        for (Tuple2<String, Object> tuple2 : tuple2s) {
            values.put(tuple2.getT1(), tuple2.getT2());
        }
        return insert(values);
    }

    /**
     * INSERT VALUES
     *
     * @param pojo
     * @return
     */
    default <T> SQLInsertOperator insertPojo(T pojo) {
        ObjectWrapper wrapper = new ObjectWrapper(pojo);
        return insert(wrapper.findAllValuesForce());
    }

    /**
     * INSERT VALUES
     *
     * @param values Key value
     * @return InsertOperator
     */
    default SQLInsertOperator insert(Map<String, Object> values) {
        Map<SQLName, Object> inserts = Maps.newHashMap();
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            inserts.put(SQLName.of(entry.getKey()), entry.getValue());
        }
        return inserts(inserts);
    }

    /**
     * INSERT VALUES
     *
     * @param values Key value
     * @return InsertOperator
     */
    SQLInsertOperator inserts(Map<SQLName, Object> values);

    /**
     * INSERT VALUES,VALUES,VALUES
     *
     * @param pojos pojos list
     * @return SQLInsertOperator
     */
    default <T> SQLInsertOperator batchInsertPojos(List<T> pojos) {
        if (CollectionUtils.isEmpty(pojos)) {
            return self();
        }
        List<Map<String, Object>> pojoDescribes = pojos.stream()
                .map(pojo -> new ObjectWrapper(pojo).findAllValuesForce())
                .collect(Collectors.toList());
        Map<String, Object> first = pojoDescribes.get(0);
        Collection<Collection<Object>> values = pojoDescribes.stream().map(Map::values).collect(Collectors.toList());
        return batchInsert(first.keySet(), values);

    }

    /**
     * INSERT VALUES,VALUES,VALUES
     *
     * @param values values
     * @return SQLInsertOperator
     */
    default SQLInsertOperator batchInsert(Collection<String> columns, Collection<Collection<Object>> values) {
        return batchInserts(columns.stream().map(SQLName::of).collect(Collectors.toList()), values);
    }

    /**
     * INSERT VALUES,VALUES,VALUES
     *
     * @param values values
     * @return SQLInsertOperator
     */
    SQLInsertOperator batchInserts(Collection<SQLName> columns, Collection<Collection<Object>> values);
}
