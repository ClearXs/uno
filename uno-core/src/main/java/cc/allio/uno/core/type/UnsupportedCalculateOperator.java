package cc.allio.uno.core.type;

/**
 * 不受支持的计算操作
 *
 * @author jiangwei
 * @date 2022/10/10 22:21
 * @since 1.1.0
 */
public abstract class UnsupportedCalculateOperator<T> implements TypeOperator<T> {

    @Override
    public T add(T origin, T passive) {
        throw new UnsupportedOperationException("Unsupported Operation 'add'");
    }

    @Override
    public T subtract(T origin, T passive) {
        throw new UnsupportedOperationException("Unsupported Operation 'subtract'");
    }

    @Override
    public T multiply(T origin, T passive) {
        throw new UnsupportedOperationException("Unsupported Operation 'multiply'");
    }

    @Override
    public T divide(T origin, T passive) {
        throw new UnsupportedOperationException("Unsupported Operation 'divide'");
    }

    @Override
    public int signum(Object target) {
        throw new UnsupportedOperationException("Unsupported Operation 'judgingPositiveAndNegative'");
    }
}
