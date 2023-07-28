package cc.allio.uno.core.type;

/**
 * Double类型转换器，可能抛出NumberFormatException异常
 *
 * @author jiangwei
 * @date 2021/12/23 20:15
 * @since 1.0
 */
public class DoubleTypeOperator implements TypeOperator {
    @Override
    public Object convert(Object target, Class<?> maybeType) {
        return Double.parseDouble(target.toString());
    }

    @Override
    public int signum(Object target) {
        double aDouble = Double.parseDouble(target.toString());
        return Double.compare(aDouble, Double.NaN);
    }

    @Override
    public String fromString(Object target) {
        return convert(target, Double.class).toString();
    }

    @Override
    public Object add(Object origin, Object passive) {
        return Double.parseDouble(origin.toString()) + Double.parseDouble(passive.toString());
    }

    @Override
    public Object subtract(Object origin, Object passive) {
        return Double.parseDouble(origin.toString()) - Double.parseDouble(passive.toString());
    }

    @Override
    public Object multiply(Object origin, Object passive) {
        return Double.parseDouble(origin.toString()) * Double.parseDouble(passive.toString());
    }

    @Override
    public Object divide(Object origin, Object passive) {
        return Double.parseDouble(origin.toString()) / Double.parseDouble(passive.toString());
    }

    @Override
    public Object defaultValue() {
        return 0d;
    }

    @Override
    public Class<?> getType() {
        return Types.DOUBLE;
    }
}
