package cc.allio.uno.netty.transport;

/**
 * screw
 * @author jiangw
 * @date 2020/12/10 17:31
 * @since 1.0
 */
public interface UnresolvedAddress {

    /**
     * 获取端口
     * @return 端口
     */
    int getPort();

    /**
     * 获取地址
     * @return 地址
     */
    String getHost();

}
