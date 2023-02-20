package cc.allio.uno.component.netty.transport.body;

import cc.allio.uno.component.netty.metadata.ServiceMetadata;
import lombok.Data;

/**
 * 订阅body
 * @author jiangw
 * @date 2020/12/10 17:31
 * @since 1.0
 */
@Data
public class SubscribeBody implements Body {

    private ServiceMetadata serviceMetadata;

}