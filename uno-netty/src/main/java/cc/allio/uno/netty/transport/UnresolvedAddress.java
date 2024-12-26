package cc.allio.uno.netty.transport;

/**
 * screw
 * @author j.x
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
