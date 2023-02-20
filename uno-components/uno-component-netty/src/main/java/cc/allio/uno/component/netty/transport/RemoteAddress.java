package cc.allio.uno.component.netty.transport;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 远程地址model
 *
 * @author jiangw
 * @date 2020/12/10 17:31
 * @since 1.0
 */
@Data
@AllArgsConstructor
public class RemoteAddress implements UnresolvedAddress {

    private final String host;

    private final int port;

}
