package cc.allio.uno.data.sql;

/**
 * 定义SQL相关联的集联操作
 *
 * @author jiangwei
 * @date 2022/9/30 13:35
 * @since 1.1.0
 */
public interface SQLAssociation {

    /**
     * 提供SQL集联操作
     *
     * @param <T>       statement泛型对象
     * @param statement statement对象
     * @return statement对象
     */
    default <T extends Statement<T>> T then(T statement) {
        return statement;
    }
}
