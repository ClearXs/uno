package cc.allio.uno.data.orm.dsl;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * SQL二元操作
 *
 * @author j.x
 * @date 2023/4/13 12:07
 * @since 1.1.4
 */
@Data
@AllArgsConstructor(staticName = "of")
public class BinaryCondition {
    // left
    private String left;
    // right
    private String right;
    // 操作符
    private TokenOperator syntax;
}
