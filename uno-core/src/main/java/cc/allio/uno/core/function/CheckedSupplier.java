package cc.allio.uno.core.function;

import org.springframework.lang.Nullable;

/**
 * 受检的 Supplier
 *
 * @author L.cm
 */
@FunctionalInterface
public interface CheckedSupplier<T> {

	/**
	 * Run the Supplier
	 *
	 * @return T
	 * @throws Throwable CheckedException
	 */
	@Nullable
	T get() throws Throwable;

}
