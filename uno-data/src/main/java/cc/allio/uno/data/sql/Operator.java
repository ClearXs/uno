package cc.allio.uno.data.sql;

/**
 * 数据操作接口
 *
 * @author jiangwei
 * @date 2023/1/5 10:37
 * @since 1.1.4
 */
public interface Operator<T extends Operator<T>> {

    /**
     * self对象
     *
     * @return
     */
    default T self() {
        return (T) this;
    }
}
