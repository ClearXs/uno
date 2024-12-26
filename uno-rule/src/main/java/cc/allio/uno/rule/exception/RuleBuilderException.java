package cc.allio.uno.rule.exception;

/**
 * Rule builder 异常
 *
 * @author j.x
 * @since 1.1.4
 */
public class RuleBuilderException extends RuntimeException {

    public RuleBuilderException() {
        super();
    }

    public RuleBuilderException(String message) {
        super(message);
    }

    public RuleBuilderException(String message, Throwable cause) {
        super(message, cause);
    }

    public RuleBuilderException(Throwable cause) {
        super(cause);
    }
}
