package cc.allio.uno.data.orm.dsl;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * SQL二元操作
 *
 * @author j.x
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
