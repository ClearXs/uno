package cc.allio.uno.core.util.type;

/**
 * 不受支持的计算操作
 *
 * @author jiangwei
 * @date 2022/10/10 22:21
 * @since 1.1.0
 */
public abstract class UnsupportedCalculateOperator implements TypeOperator {

    @Override
    public Object add(Object origin, Object passive) {
        throw new UnsupportedOperationException("Unsupported Operation 'add'");
    }

    @Override
    public Object subtract(Object origin, Object passive) {
        throw new UnsupportedOperationException("Unsupported Operation 'subtract'");
    }

    @Override
    public Object multiply(Object origin, Object passive) {
        throw new UnsupportedOperationException("Unsupported Operation 'multiply'");
    }

    @Override
    public Object divide(Object origin, Object passive) {
        throw new UnsupportedOperationException("Unsupported Operation 'divide'");
    }

    @Override
    public int signum(Object target) {
        throw new UnsupportedOperationException("Unsupported Operation 'judgingPositiveAndNegative'");
    }
}
