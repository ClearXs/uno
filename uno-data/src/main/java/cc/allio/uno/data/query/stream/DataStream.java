package cc.allio.uno.data.query.stream;

import cc.allio.uno.data.query.QueryFilter;
import cc.allio.uno.core.bean.ObjectWrapper;
import cc.allio.uno.core.util.type.TypeOperatorFactory;

import java.util.Arrays;

/**
 * 定义数据Query流，设计参考自{@link java.io.InputStream}
 *
 * @author jiangwei
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
    default void setDataFieldsData(String[] dataFields, ObjectWrapper incrementWrapper) {
        Arrays.stream(dataFields).
                filter(incrementWrapper::contains)
                .map(incrementWrapper::find)
                .forEach(property -> {
                    String name = property.getName();
                    Class<?> propertyType = property.getPropertyType();
                    try {
                        incrementWrapper.setForce(name, TypeOperatorFactory.translator(propertyType).defaultValue());
                    } catch (Throwable ex) {
                        // ignore
                    }
                });
    }
}
