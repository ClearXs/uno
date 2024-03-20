package cc.allio.uno.core.function.lambda;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 静态方法引用..
 * <pre>
 *     StaticMethodReference reference = User::getName;
 * </pre>
 *
 * @author j.x
 * @date 2023/1/5 15:06
 * @since 1.1.4
 */
@FunctionalInterface
public interface StaticMethodReference<T> extends Function<T, Object>, Serializable {
}
