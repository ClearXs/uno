package cc.allio.uno.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 端点key
 *
 * @author j.x
 * @date 2022/7/29 16:32
 * @since 1.0
 */
@Data
@AllArgsConstructor
public class EndpointKey {

    /**
     * 某个websocket连接
     */
    private String key;
}
