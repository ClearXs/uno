package cc.allio.uno.data.query.stream;

import cc.allio.uno.core.bean.ValueWrapper;
import cc.allio.uno.core.type.TypeOperatorFactory;
import cc.allio.uno.data.query.QueryFilter;

import java.util.Arrays;

/**
 * 定义数据Query流，设计参考自{@link java.io.InputStream}
 *
 * @author j.x
 * @date 2022/11/16 12:01
 * @since 1.1.0
 */
@FunctionalInterface
public interface DataStream<T> {

    /**
     * <b>模板方法</b>.
     * <p>数据读出，由实现类定义</p>
     *
     * @return 返回List数据，数据项在不同实现类具有描述
     */
    T read(QueryFilter queryFilter) throws Throwable;

    /**
     * 设置增量标识数据
     *
     * @param dataFields       数据标识字段
     * @param incrementWrapper 实体操作器
     */
    default void setDataFieldsData(String[] dataFields, ValueWrapper incrementWrapper) {
        Arrays.stream(dataFields).
                filter(incrementWrapper::contains)
                .map(incrementWrapper::find)
                .forEach(property -> {
                    String name = property.getName();
                    Class<?> instanceType;
                    Class<?> propertyType = property.getPropertyType();
                    Class<?> propertyEditorClass = property.getPropertyEditorClass();
                    if (propertyType == null) {
                        instanceType = propertyEditorClass;
                    } else {
                        instanceType = propertyType;
                    }
                    if (instanceType == null) {
                        throw new IllegalArgumentException(
                                String.format("value instance %s can't expect time type", incrementWrapper.getTarget()));
                    }
                    try {
                        incrementWrapper.setForce(name, TypeOperatorFactory.translator(instanceType).defaultValue());
                    } catch (Throwable ex) {
                        // ignore
                    }
                });
    }
}
