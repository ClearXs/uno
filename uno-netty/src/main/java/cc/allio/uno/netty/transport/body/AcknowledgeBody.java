package cc.allio.uno.netty.transport.body;

import lombok.Data;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Ack确认消息
 *
 * @author jiangw
 * @date 2020/11/30 13:37
 * @since 1.0
 */
@Data
public class AcknowledgeBody implements Body {

    private static final AtomicLong NEXT = new AtomicLong(0);

    private final long sequence;

    /**
     * 处理是否成功
     */
    private boolean isSuccess;

    public AcknowledgeBody(boolean isSuccess) {
        this(NEXT.getAndIncrement(), isSuccess);
    }

    public AcknowledgeBody(long sequence, boolean isSuccess) {
        this.sequence = sequence;
        this.isSuccess = isSuccess;
    }
}
