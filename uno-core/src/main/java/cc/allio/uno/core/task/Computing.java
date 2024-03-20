package cc.allio.uno.core.task;

import cc.allio.uno.core.StringPool;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 计算任务
 *
 * @author j.x
 * @date 2021/12/22 15:31
 * @since 1.0
 */
@FunctionalInterface
public interface Computing<T> extends Serializable {

    /**
     * 依据缓存数据执行计算逻辑
     *
     * @param buffer  缓存数据
     * @param current 当前数据，默认为缓存末尾位置的数据
     * @throws Exception 当计算过程存在异常时抛出
     */
    void calculate(List<T> buffer, T current) throws Exception;

    /**
     * 依据缓存数据执行计算逻辑，使用空集合作为缓存的数据
     *
     * @throws Exception 当计算过程存在异常时抛出
     * @see #calculate(List)
     */
    default void calculate() throws Exception {
        calculate(Collections.emptyList());
    }

    /**
     * 依据缓存数据执行计算逻辑，其中当前数据空
     *
     * @param buffer 缓存数据
     * @throws Exception 当计算过程存在异常时抛出
     */
    default void calculate(List<T> buffer) throws Exception {
        calculate(buffer, null);
    }

    /**
     * 获取计算的描述
     *
     * @return 具体的名称
     */
    default String getComputingName() {
        return Thread.currentThread().getName().concat(StringPool.COLON).concat(getClass().getName());
    }
}
