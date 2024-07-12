package cc.allio.uno.core.type;

/**
 * Integer类型转换器，可能抛出NumberFormatException异常
 *
 * @author j.x
 * @date 2021/12/23 20:08
 * @since 1.0
 */
public class IntegerTypeOperator implements TypeOperator<Integer> {

    @Override
    public Integer convert(Object target, Class<?> maybeType) {
        if (target instanceof Integer) {
            return (Integer) target;
        } else if (target instanceof Double) {
            return ((Double) target).intValue();
        } else if (target instanceof Float) {
            return ((Float) target).intValue();
        } else if (target instanceof Short) {
            return ((Short) target).intValue();
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
