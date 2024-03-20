package cc.allio.uno.rule.exception;

/**
 * 结果集超时异常
 *
 * @author j.x
 * @date 2023/4/25 10:46
 * @since 1.1.4
 */
public class RuleResultTimeoutException extends RuntimeException {

    public RuleResultTimeoutException() {
        super();
    }

    public RuleResultTimeoutException(String message) {
        super(message);
    }

    public RuleResultTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public RuleResultTimeoutException(Throwable cause) {
        super(cause);
    }
}
