package cc.allio.uno.netty.config;

import cc.allio.uno.netty.transport.UnresolvedAddress;

/**
 * netty-Client的一些配置项
 * 1.地址
 * 2.工作线程核心数
 * @author j.x
 * @since 1.0
 */
public class NettyClientConfig {

    private UnresolvedAddress defaultAddress;
    private final int workThreads = Runtime.getRuntime().availableProcessors() << 1;

    public NettyClientConfig(UnresolvedAddress address) {
        this.defaultAddress = address;
    }

    public UnresolvedAddress getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(UnresolvedAddress defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    public int getWorkThreads() {
        return workThreads;
    }
}
