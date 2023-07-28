package cc.allio.uno.core.type;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 默认类型实现
 *
 * @author jiangwei
 * @date 2023/4/12 09:23
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
