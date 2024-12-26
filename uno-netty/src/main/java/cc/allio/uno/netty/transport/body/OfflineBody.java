package cc.allio.uno.netty.transport.body;

import cc.allio.uno.netty.metadata.RegisterMetadata;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 下线body
 * @author j.x
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfflineBody implements Body {

    private RegisterMetadata registerMetadata;

}
