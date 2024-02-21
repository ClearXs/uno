package cc.allio.uno.core.api;

/**
 * 通用适配器定义
 *
 * @author jiangwei
 * @date 2024/1/9 16:04
 * @since 1.1.6
 */
public interface Adapter<R, O> {

    /**
     * 根据原始的类型获取目标转换的类型
     *
     * @param o 原始类型
     * @return 转换类型
     */
    R adapt(O o);

    /**
     * 根据目标的类型获取原始的类型
     *
     * @param r 目标类型
     * @return 原始类型
     */
    O reverse(R r);
}
