package cc.allio.uno.data.sql;

import cc.allio.uno.core.StringPool;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * SQL alias
 * <ul>
 *     <li>select filed_name as 'alias'</li>
 *     <li>count(field_name) 'alias'</li>
 *     <li>from table_name 'alias'</li>
 *     <li>etc..</li>
 * </ul>
 *
 * @author jiangwei
 * @date 2023/1/12 17:45
 * @since 1.1.4
 */
@AllArgsConstructor
public class Alias {

    @Getter
    private final String alias;

    /**
     * 返回带'的别名
     *
     * @return 'alias'
     */
    public String toQuoteAlias() {
        return StringPool.SINGLE_QUOTE + alias + StringPool.SINGLE_QUOTE;
    }
}
