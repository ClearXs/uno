package cc.allio.uno.core.type;

/**
 * <b>定义数据类型</b><br/>
 * 使用场景如下：
 * <ul>
 *     <li>通过SPI加载单例对象</li>
 * </ul>
 *
 * @author j.x
 * @since 1.0
 */
public interface Type {

    /**
     * 获取类型标识
     *
     * @return 标识
     */
    String getCode();

}
