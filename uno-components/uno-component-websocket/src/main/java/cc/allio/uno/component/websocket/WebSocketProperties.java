package cc.allio.uno.component.websocket;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("allio.uno.websocket")
public class WebSocketProperties {

    /**
     * 是否开启token校验，默认false
     */
    private boolean enableTokenValid = false;

    /**
     * websocket心跳时间，cron表达式，默认10分钟
     */
    private String heatbetasTimeout = "0 0/10 * * * ?";

    /**
     * 时序数据收集推送时间，达到定时后，把数据推送至客户端，cron表达式，默认30s。
     */
    private String collectTime = "0/30 * * * * ?";

    /**
     * 全局心跳
     * <p>
     * 心跳检测以{@link WebSocketEndpoint#enableHeartbeat()}为高优先级，再来判定全局心跳是否为true决定是否开启心跳
     * </p>
     */
    private boolean globalHeartbeats = true;
}
