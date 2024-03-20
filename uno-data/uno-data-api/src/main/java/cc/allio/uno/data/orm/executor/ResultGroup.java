package cc.allio.uno.data.orm.executor;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.type.Types;
import cc.allio.uno.core.util.ClassUtils;
import cc.allio.uno.core.util.JsonUtils;
import cc.allio.uno.core.function.lambda.MethodReferenceColumn;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.helper.PojoWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;

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
 * @author j.x
 * @date 2023/4/14 18:02
 * @since 1.1.4
 */
@Data
public class ResultGroup {

    private static DSLName.NameFeature lowCaseAndHumpNameFeature = DSLName.NameFeature.aggregate(DSLName.LOWER_CASE_FEATURE, DSLName.HUMP_FEATURE);
    private List<ResultRow> resultRows = Lists.newArrayList();
    // 以小写、驼峰作为key
    private Map<String, ResultRow> resultRowMap = Maps.newHashMap();

    /**
     * 添加{@link ResultRow}
     *
     * @param row row
     */
    public void addRow(ResultRow row) {
        resultRows.add(row.getIndex(), row);
        resultRowMap.put(row.getColumn().format(lowCaseAndHumpNameFeature), row);
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
     * 基于{@link DSLName}获取。其中{@link DSLName#format(DSLName.NameFeature)}使用{@link #lowCaseAndHumpNameFeature}先转小写在转驼峰。
     * <p>保持与map集合中key的存储方式一致</p>
     *
     * @param columnName 字段名称
     * @return ResultRow
     */
    public ResultRow getRow(DSLName columnName) {
        return resultRowMap.get(columnName.format(lowCaseAndHumpNameFeature));
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
        PojoWrapper<T> wrapper = PojoWrapper.getInstance(metadata);
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
     * @see #getOptionStringValue(String, Consumer)
     */
    public void getOptionStringValue(DSLName columnName, Consumer<String> acceptor) {
        getOptionValue(columnName, Types::toString, acceptor);
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
     * @see #getOptionValue(String, Function, Consumer)
     */
    public <R> void getOptionValue(DSLName columnName, Function<Object, R> transfer, Consumer<R> acceptor) {
        Optional.ofNullable(getRow(columnName))
                .map(ResultRow::getValue)
                .map(transfer)
                .ifPresent(acceptor);
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
    public Integer getIntegerValue(String columnName) {
        return getValue(columnName, Types::parseInteger, () -> null);
    }

    /**
     * @see #getValue(String, Function, Supplier)
     */
    public Integer getIntegerValue(DSLName columnName) {
        return getValue(columnName, Types::parseInteger, () -> null);
    }

    /**
     * @see #getValue(String, Function, Supplier)
     */
    public Integer getIntegerValue(String columnName, Supplier<Integer> defaultValue) {
        return getValue(columnName, Types::parseInteger, defaultValue);
    }

    /**
     * @see #getValue(String, Function, Supplier)
     */
    public Integer getIntegerValue(DSLName columnName, Supplier<Integer> defaultValue) {
        return getValue(columnName, Types::parseInteger, defaultValue);
    }

    /**
     * @see #getValue(String, Function, Supplier, Function)
     */
    public <K> K getIntegerValue(String columnName, Supplier<Integer> defaultValue, Function<Integer, K> adjuster) {
        return getValue(columnName, Types::parseInteger, defaultValue, adjuster);
    }

    /**
     * @see #getValue(String, Function, Supplier, Function)
     */
    public <K> K getIntegerValue(DSLName columnName, Supplier<Integer> defaultValue, Function<Integer, K> adjuster) {
        return getValue(columnName, Types::parseInteger, defaultValue, adjuster);
    }

    /**
     * @see #getValue(String, Function, Supplier)
     */
    public Long getLongValue(String columnName) {
        return getValue(columnName, Types::parseLong, () -> null);
    }

    /**
     * @see #getValue(String, Function, Supplier)
     */
    public Long getLongValue(DSLName columnName) {
        return getValue(columnName, Types::parseLong, () -> null);
    }

    /**
     * @see #getValue(String, Function, Supplier)
     */
    public Long getLongValue(String columnName, Supplier<Long> defaultValue) {
        return getValue(columnName, Types::parseLong, defaultValue);
    }

    /**
     * @see #getValue(String, Function, Supplier)
     */
    public Long getLongValue(DSLName columnName, Supplier<Long> defaultValue) {
        return getValue(columnName, Types::parseLong, defaultValue);
    }

    /**
     * @see #getValue(String, Function, Supplier, Function)
     */
    public <K> K getLongValue(String columnName, Supplier<Long> defaultValue, Function<Long, K> adjuster) {
        return getValue(columnName, Types::parseLong, defaultValue, adjuster);
    }

    /**
     * @see #getValue(String, Function, Supplier, Function)
     */
    public <K> K getLongValue(DSLName columnName, Supplier<Long> defaultValue, Function<Long, K> adjuster) {
        return getValue(columnName, Types::parseLong, defaultValue, adjuster);
    }

    /**
     * @see #getValue(String, Function, Supplier)
     */
    public Boolean getBoolValue(String columnName) {
        return getBoolValue(columnName, () -> false);
    }

    /**
     * @see #getValue(String, Function, Supplier)
     */
    public Boolean getBoolValue(DSLName columnName) {
        return getBoolValue(columnName, () -> false);
    }

    /**
     * @see #getValue(String, Function, Supplier)
     */
    public Boolean getBoolValue(String columnName, Supplier<Boolean> defaultValue) {
        return getValue(columnName, Types::parseBoolean, defaultValue);
    }

    /**
     * @see #getValue(String, Function, Supplier)
     */
    public Boolean getBoolValue(DSLName columnName, Supplier<Boolean> defaultValue) {
        return getValue(columnName, Types::parseBoolean, defaultValue);
    }

    /**
     * @see #getValue(String, Function, Supplier, Function)
     */
    public <K> K getBoolValue(String columnName, Supplier<Boolean> defaultValue, Function<Boolean, K> adjuster) {
        return getValue(columnName, Types::parseBoolean, defaultValue, adjuster);
    }

    /**
     * @see #getValue(String, Function, Supplier, Function)
     */
    public <K> K getBoolValue(DSLName columnName, Supplier<Boolean> defaultValue, Function<Boolean, K> adjuster) {
        return getValue(columnName, Types::parseBoolean, defaultValue, adjuster);
    }

    /**
     * <b>default value return empty string</b>
     *
     * @see #getValue(String, Function, Supplier)
     */
    public String getStringValue(String columnName) {
        return getStringValue(columnName, () -> StringPool.EMPTY);
    }

    /**
     * <b>default value return empty string</b>
     *
     * @see #getValue(String, Function, Supplier)
     */
    public String getStringValue(DSLName columnName) {
        return getStringValue(columnName, () -> StringPool.EMPTY);
    }

    /**
     * @see #getValue(String, Function, Supplier)
     */
    public String getStringValue(String columnName, Supplier<String> defaultValue) {
        return getValue(columnName, Types::toString, defaultValue);
    }

    /**
     * @see #getValue(String, Function, Supplier)
     */
    public String getStringValue(DSLName columnName, Supplier<String> defaultValue) {
        return getValue(columnName, Types::toString, defaultValue);
    }

    /**
     * @see #getValue(String, Function, Supplier, Function)
     */
    public <K> K getStringValue(String columnName, Supplier<String> defaultValue, Function<String, K> adjuster) {
        return getValue(columnName, Types::toString, defaultValue, adjuster);
    }

    /**
     * @see #getValue(String, Function, Supplier, Function)
     */
    public <K> K getStringValue(DSLName columnName, Supplier<String> defaultValue, Function<String, K> adjuster) {
        return getValue(columnName, Types::toString, defaultValue, adjuster);
    }

    /**
     * <b>default value 返回null</b>
     *
     * @see #getValue(DSLName, Function, Supplier, Function)
     */
    public Object getValue(String columnName) {
        return getValue(columnName, f -> f, () -> null, f -> f);
    }

    /**
     * @see #getValue(DSLName, Function, Supplier, Function)
     */
    public Object getValue(String columnName, Supplier<Object> defaultValue) {
        return getValue(columnName, f -> f, defaultValue, f -> f);
    }

    /**
     * @see #getValue(DSLName, Function, Supplier, Function)
     */
    public <R> R getValue(String columnName, Function<Object, R> transfer, Supplier<R> defaultValue) {
        return getValue(columnName, transfer, defaultValue, f -> f);
    }

    /**
     * @see #getValue(DSLName, Function, Supplier, Function)
     */
    public <R> R getValue(DSLName columnName, Function<Object, R> transfer, Supplier<R> defaultValue) {
        return getValue(columnName, transfer, defaultValue, f -> f);
    }

    /**
     * @see #getValue(DSLName, Function, Supplier, Function)
     */
    public <R, K> K getValue(String columnName, Function<Object, R> transfer, Supplier<R> defaultValue, Function<R, K> adjuster) {
        return getValue(DSLName.of(columnName), transfer, defaultValue, adjuster);
    }

    /**
     * 获取字段值，提供字段转换器，如果为null或者不存在则使用default Value
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
    public <R, K> K getValue(DSLName columnName, Function<Object, R> transfer, Supplier<R> defaultValue, Function<R, K> adjuster) {
        return Optional.ofNullable(getRow(columnName))
                .map(ResultRow::getValue)
                .map(transfer)
                .or(() -> Optional.ofNullable(defaultValue.get()))
                .map(adjuster)
                .orElse(null);
    }
}
