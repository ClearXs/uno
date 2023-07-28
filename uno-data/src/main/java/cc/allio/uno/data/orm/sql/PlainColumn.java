package cc.allio.uno.data.orm.sql;

/**
 * PLAIN_FEATURE column
 *
 * @author jiangwei
 * @date 2023/1/6 11:31
 * @since 1.1.4
 * @deprecated 1.1.4版本删除
 */
@Deprecated
public class PlainColumn extends RuntimeColumn {

    public PlainColumn(String name, Object[] value, Condition condition) {
        super(name, value, condition);
    }
}
