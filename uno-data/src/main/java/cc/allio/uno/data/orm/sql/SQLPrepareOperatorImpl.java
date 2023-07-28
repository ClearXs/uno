package cc.allio.uno.data.orm.sql;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * SQLPrepareOperatorImpl
 *
 * @author jiangwei
 * @date 2023/4/16 18:19
 * @since 1.1.4
 */
public abstract class SQLPrepareOperatorImpl<T extends SQLPrepareOperator<T>> implements SQLPrepareOperator<T> {
    private static final int DEFAULT_CAPACITY = 0;
    protected PrepareValue[] prepareValues;
    protected int prepareIndex;

    protected SQLPrepareOperatorImpl() {
        this.prepareValues = new PrepareValue[DEFAULT_CAPACITY];
        this.prepareIndex = 0;
    }

    /**
     * 添加预处理SQL
     *
     * @param value value
     */
    protected void addPrepareValue(String column, Object value) {
        // ensure数组容量
        ensureCapacityInternal(prepareValues.length + 1);
        prepareValues[prepareIndex++] = PrepareValue.of(prepareIndex, column, value);
    }

    @Override
    public List<PrepareValue> getPrepareValues() {
        return Collections.unmodifiableList(Lists.newArrayList(prepareValues));
    }

    @Override
    public void reset() {
        prepareValues = new PrepareValue[DEFAULT_CAPACITY];
        prepareIndex = 0;
    }

    protected void ensureCapacityInternal(int minCapacity) {
        ensureExplicitCapacity(minCapacity);
    }

    private void ensureExplicitCapacity(int minCapacity) {

        // overflow-conscious code
        if (minCapacity - prepareValues.length > 0)
            grow(minCapacity);
    }

    /**
     * Increases the capacity to ensure that it can hold at least the
     * number of elements specified by the minimum capacity argument.
     *
     * @param minCapacity the desired minimum capacity
     */
    private void grow(int minCapacity) {
        // overflow-conscious code
        // minCapacity is usually close to size, so this is a win:
        prepareValues = Arrays.copyOf(prepareValues, minCapacity);
    }

}
