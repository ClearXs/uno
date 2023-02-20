package cc.allio.uno.core.util.type;

/**
 * Integer类型转换器，可能抛出NumberFormatException异常
 *
 * @author jiangwei
 * @date 2021/12/23 20:08
 * @since 1.0
 */
public class IntegerTypeOperator implements TypeOperator {

    @Override
    public Object convert(Object target, Class<?> maybeType) {
        return Integer.parseInt(target.toString());
    }

    @Override
    public int signum(Object target) {
        int anInt = Integer.parseInt(target.toString());
        return Integer.signum(anInt);
    }

    @Override
    public String fromString(Object target) {
        return convert(target, Integer.class).toString();
    }

    @Override
    public Object add(Object origin, Object passive) {
        return Integer.parseInt(origin.toString()) + Integer.parseInt(passive.toString());
    }

    @Override
    public Object subtract(Object origin, Object passive) {
        return Integer.parseInt(origin.toString()) - Integer.parseInt(passive.toString());
    }

    @Override
    public Object multiply(Object origin, Object passive) {
        return Integer.parseInt(origin.toString()) * Integer.parseInt(passive.toString());
    }

    @Override
    public Object divide(Object origin, Object passive) {
        return Integer.parseInt(origin.toString()) / Integer.parseInt(passive.toString());
    }

    @Override
    public Object defaultValue() {
        return 0;
    }
}
