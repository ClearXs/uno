package cc.allio.uno.core.function.lambda;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * 对象方法引用
 * <pre>
 *     User user = new User();</br>
 *     MethodReference reference = user::getName
 * </pre>
 *
 * @author j.x
 * @date 2023/1/5 15:07
 * @since 1.1.4
 */
public interface MethodReference<T> extends Supplier<T>, Serializable {
}
