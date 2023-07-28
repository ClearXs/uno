package cc.allio.uno.data.orm;

/**
 * SQL适配器定义
 *
 * @param <R> 转换的类型
 * @param <O> 原始类型
 * @author jiangwei
 * @date 2023/4/13 13:14
 * @since 1.1.4
 */
public interface SQLAdapter<R, O> {

    /**
     * 根据原始的类型获取目标转换的类型
     *
     * @param o 原始类型实例
     * @return 转换的实例
     */
    R get(O o);

    /**
     * 根据目标的类型获取原始的类型
     *
     * @param r 目标类型
     * @return 原始类型
     */
    O reversal(R r);
}
