package cc.allio.uno.data.orm.dsl.dml;

import cc.allio.uno.core.function.lambda.MethodReferenceColumn;
import cc.allio.uno.core.util.id.IdGenerator;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.helper.PojoWrapper;
import cc.allio.uno.data.orm.dsl.opeartorgroup.Operators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.*;
import java.util.function.Supplier;

/**
 * INSERT Operator。
 * <p><b>值得注意的是每调用一次{@link #insert(String, Object)}API，都会生成一个VALUES。建议调用{@link #batchInserts(List, List)}API批量生成</b></p>
 *
 * @author j.x
 * @date 2023/4/13 15:25
 * @see Operators
 * @since 1.1.4
 */
public interface InsertOperator<T extends InsertOperator<T>> extends PrepareOperator<T>, TableOperator<T> {

    /**
     * INSERT VALUES
     *
     * @param reference key
     * @param value     value
     * @return self
     */
    default <R> T insert(MethodReferenceColumn<R> reference, Object value) {
        return insert(reference.getColumn(), value);
    }

    /**
     * INSERT VALUES
     *
     * @param fieldName fieldName
     * @param value     value
     * @return self
     */
    default T insert(String fieldName, Object value) {
        return insert(Map.of(fieldName, getValueIfNull(value)));
    }

    /**
     * INSERT VALUES
     *
     * @param f1 f1
     * @param v1 v1
     * @return self
     */
    default T insert(String f1, Object v1, String f2, Object v2) {
        return insert(Tuples.of(f1, getValueIfNull(v1)), Tuples.of(f2, getValueIfNull(v2)));
    }

    /**
     * INSERT VALUES
     *
     * @param f1 f1
     * @param v1 v1
     * @param f2 f2
     * @param v2 v2
     * @return self
     */
    default T insert(String f1, Object v1, String f2, Object v2, String f3, Object v3) {
        return insert(
                Tuples.of(f1, getValueIfNull(v1)),
                Tuples.of(f2, getValueIfNull(v2)),
                Tuples.of(f3, getValueIfNull(v3)));
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
     * @return self
     */
    default T insert(String f1, Object v1, String f2, Object v2, String f3, Object v3, String f4, Object v4) {
        return insert(
                Tuples.of(f1, getValueIfNull(v1)),
                Tuples.of(f2, getValueIfNull(v2)),
                Tuples.of(f3, getValueIfNull(v3)),
                Tuples.of(f4, getValueIfNull(v4)));
    }

    /**
     * INSERT VALUES
     *
     * @param tuple2s Key value
     * @return self
     */
    default T insert(Tuple2<String, Object>... tuple2s) {
        Map<String, Object> values = Maps.newHashMap();
        for (Tuple2<String, Object> tuple2 : tuple2s) {
            values.put(tuple2.getT1(), tuple2.getT2());
        }
        return insert(values);
    }

    /**
     * 基于POJO实体动态构建INSERT VALUES，如果该实体某个字段没有值将不会加入到INSERT中
     *
     * @param pojo pojo
     * @return self
     */
    default <P> T insertPojo(P pojo) {
        PojoWrapper<P> pojoWrapper = PojoWrapper.getInstance(pojo);
        String pkName = pojoWrapper.getPkColumn().getDslName().format(DSLName.HUMP_FEATURE);
        ColumnDef pkColumn = pojoWrapper.getPkColumn();
        Object pkValue = pkColumn.castValue(IdGenerator.defaultGenerator().getNextId());
        pojoWrapper.setForceCoverage(pkName, false, pkValue);
        Collection<DSLName> columns = pojoWrapper.getColumnDSLName();
        List<Object> values = pojoWrapper.getColumnValues();
        return columns(columns).values(values);
    }

    /**
     * INSERT VALUES
     *
     * @param values Key value
     * @return self
     */
    default T insert(Map<String, Object> values) {
        Map<DSLName, Object> inserts = Maps.newLinkedHashMap();
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            inserts.put(DSLName.of(entry.getKey()), getValueIfNull(entry.getValue()));
        }
        return inserts(inserts);
    }

    /**
     * INSERT VALUES
     *
     * @param values Key value
     * @return self
     */
    default T inserts(Map<DSLName, Object> values) {
        Set<DSLName> sqlNames = values.keySet();
        Collection<Object> value = values.values();
        return columns(sqlNames.stream().toList()).values(Lists.newArrayList(value));
    }

    /**
     * 基于POJO实体动态构建INSERT VALUES，如果该实体某个字段没有值将不会加入到INSERT中
     *
     * @param pojos pojos list
     * @return self
     */
    default <P> T batchInsertPojos(List<P> pojos) {
        for (P pojo : pojos) {
            insertPojo(pojo);
        }
        return self();
    }

    /**
     * INSERT VALUES,VALUES,VALUES
     *
     * @param values values
     * @return self
     */
    default T batchInsert(List<String> columns, List<List<Object>> values) {
        return batchInserts(columns.stream().map(DSLName::of).toList(), values);
    }

    /**
     * INSERT VALUES,VALUES,VALUES
     *
     * @param values values
     * @return self
     */
    default T batchInserts(List<DSLName> columns, List<List<Object>> values) {
        columns(columns);
        for (List<Object> value : values) {
            values(value);
        }
        return self();
    }

    /**
     * @see #strictFill(String, Supplier)
     */
    default T strictFill(String f, Object v) {
        return strictFill(f, () -> v);
    }

    /**
     * 当使用了{@link #insert(Map)}或者{@link #batchInserts(List, List)}等插入数据API时，
     * 如果需要对某一个或多个字段进行其他方式的设置值，此时需要调用该API进行重新填充。
     * <p>使用该API的值懒加载，可以避免相同字段都是同一值的情况.</p>
     *
     * @param f 字段名
     * @param v 字段值
     * @return self
     */
    T strictFill(String f, Supplier<Object> v);

    /**
     * 提供insert column
     *
     * @param columns columns
     * @return self
     */
    default T columns(String... columns) {
        return columns(Lists.newArrayList(columns).stream().map(DSLName::of).toList());
    }

    /**
     * 提供insert column
     *
     * @param columns columns
     * @return self
     */
    default T columns(DSLName... columns) {
        return columns(Lists.newArrayList(columns));
    }

    /**
     * 提供insert column
     *
     * @param columns columns
     * @return self
     */
    T columns(Collection<DSLName> columns);

    /**
     * @see #values(List)
     */
    default T values(Object... values) {
        return values(Lists.newArrayList(values));
    }

    /**
     * 提供insert values。
     * <ul>
     *     <li>调用该API前提需要调用{@link #columns(DSLName...)}生成insert column</li>
     *     <li>每调用一次生成一条数据</li>
     *     <li>如果values的长度大于columns的长度，会忽略values的最后的值</li>
     *     <li>如果values的长度小于columns的长度，会使用null进行填充</li>
     *     <li>如果两次或多次values之间有使用{@link #columns(DSLName...)}，那么在使用{@link #columns(DSLName...)}之前之后的值将以新的column作为基础</li>
     * </ul>
     *
     * @param values values
     * @return self
     */
    T values(List<Object> values);

    /**
     * 是否是批量插入
     *
     * @return true is
     */
    boolean isBatched();
}
