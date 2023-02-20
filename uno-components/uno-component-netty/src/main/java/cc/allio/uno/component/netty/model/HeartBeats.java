package cc.allio.uno.component.netty.model;

import cc.allio.uno.component.netty.Protocol;

import java.util.concurrent.TimeUnit;

/**
 * 心跳
 * @author jiangw
 */
public class HeartBeats {

    private static HeartBeatsConfig heartBeatsConfig;

    static {
        heartBeatsConfig = new HeartBeatsConfig();
    }

    public static RemoteTransporter heartbeat() {
        return RemoteTransporter.createRemoteTransporter(Protocol.Code.HEART_BEATS, null);
    }

    public static HeartBeatsConfig config() {
        return heartBeatsConfig;
    }

    /**
     * 心跳的一些配置参数
     */
    public static class HeartBeatsConfig {

        /**
         * 如果服务器端超过这个时间没有读到数据，触发{@link io.netty.handler.timeout.IdleState READER_IDLE 事件}
         */
        private int readIdleTime = 60;

        /**
         * 如果客户端超过这个事件没有写数据，触发{@link io.netty.handler.timeout.IdleState WRITER_IDLE 事件}
         * 需要<readIdleTime时间.如果是>=readIdleTime，那么会出现，客户端还没有还是发心跳包，服务方就可能将客户端的连接关闭。
         */
        private int writeIdleTime = 50;

        /**
         * 空闲读写单位
         */
        private TimeUnit unit = TimeUnit.SECONDS;

        /**
         * 服务器端可接受空闲读的次数 即当readIdleTime * accept时间后将关闭与客户端的通道
         */
        private int accept = 2;

        public int getReadIdleTime() {
            if (writeIdleTime >= readIdleTime) {
                throw new IllegalArgumentException("readIdleTime(" + readIdleTime + ") must greater than writeIdleTime(" + writeIdleTime + ")");
            }
            return readIdleTime;
        }

        public void setReadIdleTime(int readIdleTime) {
            this.readIdleTime = readIdleTime;
        }

        public int getWriteIdleTime() {
            return writeIdleTime;
        }

        public void setWriteIdleTime(int writeIdleTime) {
            this.writeIdleTime = writeIdleTime;
        }

        public TimeUnit getUnit() {
            return unit;
        }

        public void setUnit(TimeUnit unit) {
            this.unit = unit;
        }

        public int getAccept() {
            return accept;
        }

        public void setAccept(int accept) {
            this.accept = accept;
        }
    }
}
