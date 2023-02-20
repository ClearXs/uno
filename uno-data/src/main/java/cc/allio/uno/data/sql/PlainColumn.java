package cc.allio.uno.data.sql;

/**
 * plain column
 *
 * @author jiangwei
 * @date 2023/1/6 11:31
 * @since 1.1.4
 */
public class PlainColumn extends RuntimeColumn {

    public PlainColumn(String name, Object[] value, Condition condition) {
        super(name, value, condition);
    }
}
