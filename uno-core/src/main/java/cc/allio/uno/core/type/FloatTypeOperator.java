package cc.allio.uno.core.type;

/**
 * Float类型转换器，可能抛出NumberFormatException异常
 *
 * @author jiangwei
 * @date 2021/12/23 20:11
 * @since 1.0
 */
public class FloatTypeOperator implements TypeOperator {
    @Override
    public Object convert(Object target, Class<?> maybeType) {
        return Float.parseFloat(target.toString());
    }

    @Override
    public int signum(Object target) {
        float aFloat = Float.parseFloat(target.toString());
        return Float.compare(aFloat, Float.NaN);
    }

    @Override
    public String fromString(Object target) {
        return convert(target, Float.class).toString();
    }

    @Override
    public Object add(Object origin, Object passive) {
        return Float.parseFloat(origin.toString()) + Float.parseFloat(passive.toString());
    }

    @Override
    public Object subtract(Object origin, Object passive) {
        return Float.parseFloat(origin.toString()) - Float.parseFloat(passive.toString());
    }

    @Override
    public Object multiply(Object origin, Object passive) {
        return Float.parseFloat(origin.toString()) * Float.parseFloat(passive.toString());
    }

    @Override
    public Object divide(Object origin, Object passive) {
        return Float.parseFloat(origin.toString()) / Float.parseFloat(passive.toString());
    }

    @Override
    public Object defaultValue() {
        return 0f;
    }

    @Override
    public Class<?> getType() {
        return Types.FLOAT;
    }
}
