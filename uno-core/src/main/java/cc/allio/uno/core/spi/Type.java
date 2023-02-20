package cc.allio.uno.core.spi;

/**
 * <b>定义数据类型</b><br/>
 * 使用场景如下：
 * <ul>
 *     <li>通过SPI加载单例对象</li>
 * </ul>
 *
 * @author jiangwei
 * @date 2022/5/20 10:26
 * @since 1.0
 */
public interface Type<T> {

    /**
     * 获取数据类型集合，该数据类型必须要实现{@link Object#equals(Object)}方法
     *
     * @return 数据类型实例
     */
    T getType();

    /**
     * 判断给定的类型是否包含在类型集合中
     *
     * @param type 校验的类型
     * @return true 包含 false 不包含
     */
    boolean contains(T type);
}
