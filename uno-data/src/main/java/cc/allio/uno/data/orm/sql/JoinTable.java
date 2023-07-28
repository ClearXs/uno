package cc.allio.uno.data.orm.sql;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * JOIN Table
 *
 * @author jiangwei
 * @date 2023/4/13 12:23
 * @since 1.1.4
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class JoinTable extends Table {

    private Table left;
    private JoinType joinType;
    private Table right;
    private TokenOperator condition;
}
