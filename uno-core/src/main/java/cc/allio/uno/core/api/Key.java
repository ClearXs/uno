package cc.allio.uno.core.api;

/**
 * 定义关键字接口
 *
 * @author j.x
 * @date 2023/4/19 11:09
 * @since 1.1.4
 */
public interface Key {

    /**
     * 获取Key的配置标识
     *
     * @return string
     */
    String getProperties();

    /**
     * 获取key标识
     *
     * @return key
     */
    String key();
}
