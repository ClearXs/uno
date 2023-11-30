package cc.allio.uno.data.orm.executor;

import cc.allio.uno.core.bean.ObjectWrapper;
import cc.allio.uno.core.util.ClassUtils;
import cc.allio.uno.core.util.JsonUtils;
import cc.allio.uno.core.function.MethodReferenceColumn;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Collection;
import java.util.List;
import java.util.Map;

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
        resultRowMap.put(row.getColumn().format(), row);
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
                wrapper.setForce(
                        resultRow.getColumn().format(), resultRow.getValue());
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
}
