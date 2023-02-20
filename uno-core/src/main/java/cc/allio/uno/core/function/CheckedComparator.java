package cc.allio.uno.core.function;

/**
 * 受检的 Comparator
 *
 * @author L.cm
 */
@FunctionalInterface
public interface CheckedComparator<T> {

	/**
	 * Compares its two arguments for order.
	 *
	 * @param o1 o1
	 * @param o2 o2
	 * @return int
	 * @throws Throwable CheckedException
	 */
	int compare(T o1, T o2) throws Throwable;

}
