package cc.allio.uno.netty.transport;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 远程地址model
 *
 * @author j.x
 * @since 1.0
 */
@Data
@AllArgsConstructor
public class RemoteAddress implements UnresolvedAddress {

    private final String host;

    private final int port;

}
