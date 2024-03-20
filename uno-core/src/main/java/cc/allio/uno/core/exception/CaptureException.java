package cc.allio.uno.core.exception;

/**
 * 定义需要可以捕获的异常皆转换为当前异常对象
 *
 * @author j.x
 * @date 2021/12/31 14:12
 * @since 1.0
 */
public class CaptureException extends RuntimeException {

	public CaptureException() {
	}

	public CaptureException(String message) {
		super(message);
	}

	public CaptureException(String message, Throwable cause) {
		super(message, cause);
	}

	public CaptureException(Throwable cause) {
		super(cause);
	}

	public CaptureException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
