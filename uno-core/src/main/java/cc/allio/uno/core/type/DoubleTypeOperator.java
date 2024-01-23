package cc.allio.uno.core.type;

/**
 * Double类型转换器，可能抛出NumberFormatException异常
 *
 * @author jiangwei
 * @date 2021/12/23 20:15
 * @since 1.0
 */
public class DoubleTypeOperator implements TypeOperator<Double> {
    @Override
    public Double convert(Object target, Class<?> maybeType) {
        return Types.parseDouble(target);
    }

    @Override
    public int signum(Object target) {
        double aDouble = convert(target);
        return Double.compare(aDouble, Double.NaN);
    }

    @Override
    public String fromString(Object target) {
        return convert(target).toString();
    }

    @Override
    public Double add(Double origin, Double passive) {
        return origin + passive;
    }

    @Override
    public Double subtract(Double origin, Double passive) {
        return origin - passive;
    }

    @Override
    public Double multiply(Double origin, Double passive) {
        return origin * passive;
    }

    @Override
    public Double divide(Double origin, Double passive) {
        return origin / passive;
    }

    @Override
    public Double defaultValue() {
        return 0d;
    }

    @Override
    public Class<?> getType() {
        return Types.DOUBLE;
    }
}
