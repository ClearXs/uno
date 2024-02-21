package cc.allio.uno.core.api;

/**
 * 数据操作接口
 *
 * @author jiangwei
 * @date 2023/1/5 10:37
 * @since 1.1.4
 */
public interface Self<T extends Self<T>> {

    /**
     * self对象
     *
     * @return
     */
    default T self() {
        return (T) this;
    }
}
