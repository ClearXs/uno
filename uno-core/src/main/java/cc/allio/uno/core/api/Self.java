package cc.allio.uno.core.api;

/**
 * 数据操作接口
 *
 * @author j.x
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
