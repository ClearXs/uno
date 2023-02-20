package cc.allio.uno.core.function;

/**
 * 受检的 runnable
 *
 * @author L.cm
 */
@FunctionalInterface
public interface CheckedRunnable {

	/**
	 * Run this runnable.
	 *
	 * @throws Throwable CheckedException
	 */
	void run() throws Throwable;

}
