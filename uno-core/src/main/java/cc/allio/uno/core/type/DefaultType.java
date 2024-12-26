package cc.allio.uno.core.type;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 默认类型实现
 *
 * @author j.x
 * @since 1.1.4
 */
@Data
@AllArgsConstructor(staticName = "of")
public class DefaultType implements Type {

    /**
     * 类型唯一标识
     */
    private String code;

    @Override
    public String getCode() {
        return code;
    }
}
