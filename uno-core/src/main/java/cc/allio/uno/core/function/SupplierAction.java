package cc.allio.uno.core.function;

import java.util.function.Supplier;

/**
 * supplier 动作
 *
 * @author jiangwei
 * @date 2024/1/9 15:58
 * @since 1.1.7
 */
public interface SupplierAction<T> extends Supplier<T>, Action<T> {
}
