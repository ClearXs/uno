package cc.allio.uno.data.orm.dsl;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 数据库信息
 *
 * @author jiangwei
 * @date 2024/1/4 18:32
 * @since 1.1.6
 */
@Data
@AllArgsConstructor(staticName = "of")
public class Database {

    /**
     * 数据库名
     */
    private DSLName name;

}
