package cc.allio.uno.component.netty.config;

import cc.allio.uno.component.netty.transport.RemoteAddress;
import cc.allio.uno.component.netty.transport.UnresolvedAddress;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 配置服务启动项
 * 1.端口
 * 2.工作反应器的线程数
 * 3.注册中心地址
 * @author jiangw
 * @date 2020/11/26 9:20
 * @since 1.0
 */
public class NettyServerConfig {

    private final int listenerPort;

    private int workerThreads = Runtime.getRuntime().availableProcessors() >> 1;

    private final UnresolvedAddress registerAddress;

    public NettyServerConfig() {
        this(8080);
    }

    public NettyServerConfig(int listenerPort) {
        this.listenerPort = listenerPort;
        String hostAddress;
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            hostAddress = "localhost";
            e.printStackTrace();
        }
        this.registerAddress = new RemoteAddress(hostAddress, listenerPort);
    }

    public NettyServerConfig(String serverAddress, int listenerPort) {
        this.listenerPort = listenerPort;
        this.registerAddress = new RemoteAddress(serverAddress, listenerPort);
    }

    public int getListenerPort() {
        return listenerPort;
    }

    public int getWorkerThreads() {
        return workerThreads;
    }

    public void setWorkerThreads(int workerThreads) {
        this.workerThreads = workerThreads;
    }

    public UnresolvedAddress getRegisterAddress() {
        return registerAddress;
    }
}

