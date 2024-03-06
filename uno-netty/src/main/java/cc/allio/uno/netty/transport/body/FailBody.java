package cc.allio.uno.netty.transport.body;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 失败body
 * @author jiangw
 * @date 2020/12/10 17:30
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FailBody implements Body {

    private Throwable cause;
}
