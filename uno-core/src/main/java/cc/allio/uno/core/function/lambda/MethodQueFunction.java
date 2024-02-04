package cc.allio.uno.core.function.lambda;

import java.io.Serializable;

/**
 * 基于函数式接口的Lambda实现，四元函数
 *
 * @author jiangwei
 * @date 2024/1/26 18:41
 * @since 1.1.6
 */
@FunctionalInterface
public interface MethodQueFunction<T1, T2, T3, T4, R> extends Serializable, LambdaMethod {

    R apply(T1 t1, T2 t2, T3 t3, T4 t4);
}
