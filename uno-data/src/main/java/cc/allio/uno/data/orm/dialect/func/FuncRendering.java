package cc.allio.uno.data.orm.dialect.func;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * func实例转换为SQL功能接口
 *
 * @author jiangwei
 * @date 2023/1/12 16:38
 * @since 1.1.4
 */
@FunctionalInterface
public interface FuncRendering {

    /**
     * 渲染当前函数为表达式字符串
     *
     * @param descriptor func descriptor
     * @param arguments  渲染过程中动态参数
     * @return 表达式
     */
    String render(FuncDescriptor descriptor, Object[] arguments);


    /**
     * 根据给定类型查找参数中是否含有，如果有则返回。
     *
     * @param type 给定的类型
     * @param <T>  类型
     * @return List
     */
    static <T> List<T> getArgument(Object[] arguments, Class<T> type) {
        return Arrays.stream(arguments).filter(a -> type.isAssignableFrom(a.getClass())).map(type::cast).collect(Collectors.toList());
    }

    /**
     * 根据给定类型查找参数中是否含有，如果有则返回单个实例
     *
     * @param type 给定的类型
     * @param <T>  类型
     * @return 实例
     */
    static <T> Optional<T> getSingleArgument(Object[] arguments, Class<T> type) {
        if (containsTypeArgument(arguments, type)) {
            return Arrays.stream(arguments).filter(a -> type.isAssignableFrom(a.getClass())).map(type::cast).findFirst();
        }
        return Optional.empty();
    }

    /**
     * 判断是否包含指定类型的参数
     *
     * @param type 类型
     * @return true 包含 false 不包含
     */
    static boolean containsTypeArgument(Object[] arguments, Class<?> type) {
        return Arrays.stream(arguments).anyMatch(a -> type.isAssignableFrom(a.getClass()));
    }
}
