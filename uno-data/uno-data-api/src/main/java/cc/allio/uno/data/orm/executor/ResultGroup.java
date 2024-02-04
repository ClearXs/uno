package cc.allio.uno.data.orm.executor;

import cc.allio.uno.core.bean.ObjectWrapper;
import cc.allio.uno.core.type.Types;
import cc.allio.uno.core.util.ClassUtils;
import cc.allio.uno.core.util.JsonUtils;
import cc.allio.uno.core.function.lambda.MethodReferenceColumn;
import cc.allio.uno.data.orm.dsl.DSLName;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import org.checkerframework.checker.units.qual.K;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 结果集。保持顺序一致性
 *
 * @author jiangwei
 * @date 2023/4/14 18:02
 * @since 1.1.4
 */
@Data
public class ResultGroup {

    private List<ResultRow> resultRows = Lists.newArrayList();
    private Map<String, ResultRow> resultRowMap = Maps.newHashMap();

    /**
     * 添加{@link ResultRow}
     *
     * @param row row
     */
    public void addRow(ResultRow row) {
        resultRows.add(row.getIndex(), row);
        resultRowMap.put(row.getColumn().format().toUpperCase(), row);
    }

    /**
     * 添加{@link ResultRow}集合
     *
     * @param resultRows the resultRows
     */
    public void addAllRows(Collection<ResultRow> resultRows) {
        resultRows.forEach(this::addRow);
    }

    /**
     * 根据结果集索引
     *
     * @param index index
     */
    public ResultRow getRow(int index) {
        return resultRows.get(index);
    }

    /**
     * 根据字段名称获取ResultRow
     *
     * @param columnName 字段名称
     * @return ResultRow
     */
    public ResultRow getRow(String columnName) {
        return resultRowMap.get(columnName);
    }

    /**
     * 根据方法索引获取字段名
     *
     * @param ref ref
     * @return ResultRow
     */
    public ResultRow getRow(MethodReferenceColumn<?> ref) {
        return getRow(ref.getColumn());
    }

    /**
     * 返回实体对象
     *
     * @param entity 实体类型
     * @param <T>    实体范型
     * @return T
     */
    public <T> T toEntity(Class<T> entity) {
        T metadata = ClassUtils.newInstance(entity);
        ObjectWrapper wrapper = new ObjectWrapper(metadata);
        for (ResultRow resultRow : resultRows) {
            Object value = resultRow.getValue();
            if (value != null) {
                String column = DSLName.of(resultRow.getColumn(), DSLName.LOWER_CASE_FEATURE, DSLName.HUMP_FEATURE).format();
                wrapper.setForce(column, value);
            }
        }
        return metadata;
    }

    /**
     * 返回map数据
     *
     * @return map
     */
    public Map<String, Object> toMap() {
        Map<String, Object> valuesMap = Maps.newHashMap();
        for (Map.Entry<String, ResultRow> entry : resultRowMap.entrySet()) {
            valuesMap.put(entry.getKey(), entry.getValue().getValue());
        }
        return valuesMap;
    }

    /**
     * 返回json数据
     *
     * @return json 字符串
     */
    public String toJson() {
        return JsonUtils.toJson(toMap());
    }

    /**
     * 获取字段值，如果存在则回调 acceptor
     *
     * @param columnName columnName
     * @param acceptor   acceptor
     */
    public void getOptionStringValue(String columnName, Consumer<String> acceptor) {
        getOptionValue(columnName, Types::toString, acceptor);
    }

    /**
     * 获取字段值，如果存在则回调 acceptor
     *
     * @param columnName columnName
     * @param transfer   提供value的Object类型转换为R类型
     * @param acceptor   acceptor
     */
    public <R> void getOptionValue(String columnName, Function<Object, R> transfer, Consumer<R> acceptor) {
        Optional.ofNullable(getRow(columnName))
                .map(ResultRow::getValue)
                .map(transfer)
                .ifPresent(acceptor);
    }

    /**
     * @see #getValue(String, Function, Supplier)
     */
    public String getStringValue(String columnName, Supplier<String> defaultValue) {
        return getValue(columnName, Types::toString, defaultValue);
    }

    /**
     * @see #getValue(String, Function, Supplier, Function)
     */
    public <K> K getStringValue(String columnName, Supplier<String> defaultValue, Function<String, K> adjuster) {
        return getValue(columnName, Types::toString, defaultValue, adjuster);
    }


    /**
     * 获取字段值，提供字段转换器，如果为null或者不存在则使用default Value
     *
     * @param columnName   columnName
     * @param transfer     提供value的Object类型转换为R类型
     * @param defaultValue 默认值
     * @param <R>          返回类型
     * @return column value or default value
     */
    public <R> R getValue(String columnName, Function<Object, R> transfer, Supplier<R> defaultValue) {
        return Optional.ofNullable(getRow(columnName))
                .map(ResultRow::getValue)
                .map(transfer)
                .orElseGet(defaultValue);
    }

    /**
     * 获取字段值，提供字段转换器，如果为null或者不存在则使用default Value，
     * <b>该api提供对返回值的再次加工</b>
     *
     * @param columnName   columnName
     * @param transfer     提供value的Object类型转换为R类型
     * @param defaultValue 默认值
     * @param adjuster     加工器，对值再次进行二次加工
     * @param <R>          返回类型
     * @param <K>          加工返回值的类型
     * @return column value or default value
     */
    public <R, K> K getValue(String columnName, Function<Object, R> transfer, Supplier<R> defaultValue, Function<R, K> adjuster) {
        R returnValue =
                Optional.ofNullable(getRow(columnName))
                        .map(ResultRow::getValue)
                        .map(transfer)
                        .orElseGet(defaultValue);
        return Optional.ofNullable(returnValue)
                .map(adjuster)
                .orElse(null);
    }
}
