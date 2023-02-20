package cc.allio.uno.component.flink;

/**
 * 输入
 *
 * @param <T> 输入数据类型
 * @author jiangwei
 * @date 2022/2/23 14:20
 * @since 1.0
 */
public interface Input<T> {

    /**
     * 向数据源中输入数据
     *
     * @param data 数据
     */
    void input(T data);
}
