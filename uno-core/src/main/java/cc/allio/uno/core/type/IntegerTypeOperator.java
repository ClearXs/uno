package cc.allio.uno.core.type;

/**
 * Integer类型转换器，可能抛出NumberFormatException异常
 *
 * @author jiangwei
 * @date 2021/12/23 20:08
 * @since 1.0
 */
public class IntegerTypeOperator implements TypeOperator<Integer> {

    @Override
    public Integer convert(Object target, Class<?> maybeType) {
        Object tryToNumeric = Types.tryToNumeric(target);
        if (Types.isDouble(tryToNumeric.getClass())) {
            return ((Double) tryToNumeric).intValue();
        } else if (Types.isInteger(tryToNumeric.getClass())) {
            return (Integer) tryToNumeric;
        }
        throw new IllegalArgumentException(String.format("target %s can't cast type %s", target, maybeType));
    }

    @Override
    public int signum(Object target) {
        int anInt = convert(target);
        return Integer.signum(anInt);
    }

    @Override
    public String fromString(Object target) {
        return convert(target).toString();
    }

    @Override
    public Integer add(Integer origin, Integer passive) {
        return origin + passive;
    }

    @Override
    public Integer subtract(Integer origin, Integer passive) {
        return origin - passive;
    }

    @Override
    public Integer multiply(Integer origin, Integer passive) {
        return origin * passive;
    }

    @Override
    public Integer divide(Integer origin, Integer passive) {
        return origin / passive;
    }

    @Override
    public Integer defaultValue() {
        return 0;
    }

    @Override
    public Class<?> getType() {
        return Types.INTEGER;
    }
}
