package cc.allio.uno.data.orm.dsl;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.List;

/**
 * SQLPrepareOperatorImpl
 *
 * @author jiangwei
 * @date 2023/4/16 18:19
 * @since 1.1.4
 */
public abstract class PrepareOperatorImpl<T extends PrepareOperator<T>> implements PrepareOperator<T> {
    private static final int DEFAULT_CAPACITY = 0;
    protected PrepareValue[] prepareValues;
    protected int prepareIndex;

    protected PrepareOperatorImpl() {
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
        ensureExplicitCapacity(prepareValues.length + 1);
        try {
            prepareValues[prepareIndex] = PrepareValue.of(prepareIndex, column, value);
        } finally {
            incrementPrepareIndex();
        }
    }

    /**
     * 根据指定index设置值
     *
     * @param index  index
     * @param column column
     * @param value  value
     */
    protected void setPrepareValue(int index, String column, Object value) {
        if (index > prepareValues.length - 1) {
            ensureExplicitCapacity(index + 1);
        }
        prepareValues[index] = PrepareValue.of(index, column, value);
    }

    @Override
    public List<PrepareValue> getPrepareValues() {
        return Lists.newArrayList(prepareValues);
    }

    @Override
    public void reset() {
        prepareValues = new PrepareValue[DEFAULT_CAPACITY];
        prepareIndex = 0;
    }

    protected void incrementPrepareIndex() {
        prepareIndex++;
    }

    /**
     * 数组从某个index开始，向后移动length位数
     *
     * @param index  index
     * @param digit digit
     */
    protected void backward(int index, int digit) {
        ensureExplicitCapacity(prepareValues.length + digit);
        ArrayUtils.shift(prepareValues, index, prepareValues.length, digit);
    }

    protected void ensureExplicitCapacity(int minCapacity) {
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
