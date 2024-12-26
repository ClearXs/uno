package cc.allio.uno.data.orm.dsl;

import cc.allio.uno.core.bean.ValueWrapper;
import cc.allio.uno.core.util.CollectionUtils;
import com.google.common.collect.Maps;

import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * SQL prepare operator
 *
 * @author j.x
 * @see PreparedStatement
 * @since 1.1.4
 */
public interface PrepareOperator<T extends PrepareOperator<T>> extends Operator<T> {

    /**
     * 获取prepare dsl，形如(select * xxxx dual where a > ?)
     *
     * @return String
     */
    String getPrepareDSL();

    /**
     * 获取prepare sql对应的参数
     *
     * @return PrepareValue-list
     */
    List<PrepareValue> getPrepareValues();

    /**
     * 获取以{@link PrepareValue#getColumn()}为key，{@link PrepareValue#getValue()}为value的map
     *
     * @return Map
     */
    default Map<String, Object> toMapValue() {
        List<PrepareValue> prepareValues = getPrepareValues();
        if (CollectionUtils.isNotEmpty(prepareValues)) {
            Map<String, Object> columnValues = Maps.newHashMap();
            for (PrepareValue prepareValue : prepareValues) {
                columnValues.put(prepareValue.getColumn(), prepareValue.getValue());
            }
            return columnValues;

        }
        return Collections.emptyMap();
    }

    /**
     * 获取{@link PrepareValue#getValue()}的list数据
     *
     * @return list
     */
    default List<Object> getListValue() {
        List<PrepareValue> prepareValues = getPrepareValues();
        if (CollectionUtils.isNotEmpty(prepareValues)) {
            return prepareValues.stream().map(PrepareValue::getValue).collect(Collectors.toList());

        }
        return Collections.emptyList();
    }

    /**
     * 获取真实值，给定的参数可能由某一些对象进行报装生成，该方法拆分该包装，还原真实的值
     *
     * @param fake fake
     * @return real value
     */
    default Object getRealityValue(Object fake) {
        if (ValueWrapper.EMPTY_VALUE.equals(fake)) {
            return null;
        }
        return fake;
    }
}
