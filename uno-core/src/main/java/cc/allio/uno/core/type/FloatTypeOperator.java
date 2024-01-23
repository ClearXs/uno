package cc.allio.uno.core.type;

/**
 * Float类型转换器，可能抛出NumberFormatException异常
 *
 * @author jiangwei
 * @date 2021/12/23 20:11
 * @since 1.0
 */
public class FloatTypeOperator implements TypeOperator<Float> {
    @Override
    public Float convert(Object target, Class<?> maybeType) {
        return Types.parseFloat(target);
    }

    @Override
    public int signum(Object target) {
        float aFloat = convert(target);
        return Float.compare(aFloat, Float.NaN);
    }

    @Override
    public String fromString(Object target) {
        return convert(target).toString();
    }

    @Override
    public Float add(Float origin, Float passive) {
        return origin + passive;
    }

    @Override
    public Float subtract(Float origin, Float passive) {
        return origin - passive;
    }

    @Override
    public Float multiply(Float origin, Float passive) {
        return origin * passive;
    }

    @Override
    public Float divide(Float origin, Float passive) {
        return origin / passive;
    }

    @Override
    public Float defaultValue() {
        return 0f;
    }

    @Override
    public Class<?> getType() {
        return Types.FLOAT;
    }
}
