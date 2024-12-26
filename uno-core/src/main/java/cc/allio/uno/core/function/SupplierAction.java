package cc.allio.uno.core.function;

import java.util.function.Supplier;

/**
 * supplier 动作
 *
 * @author j.x
 * @since 1.1.7
 */
public interface SupplierAction<T> extends Supplier<T>, Action<T> {
}
