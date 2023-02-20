package cc.allio.uno.component.netty.config;

public interface GlobeConfig {

    /**
     * 连接超时时间
     */
    int CONNECT_TIMEOUT_MILLIS = 30000;

    /**
     * 最大等待连接时长
     */
    long MAX_DELAY_TIMED = 86400000L;

}
