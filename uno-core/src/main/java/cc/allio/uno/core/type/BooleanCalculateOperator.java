package cc.allio.uno.core.type;

/**
 * Boolean类型转换器，可能抛出NumberFormatException异常
 *
 * @author j.x
 * @since 1.0
 */
public class BooleanCalculateOperator extends UnsupportedCalculateOperator<Boolean> {
    @Override
    public Boolean convert(Object target, Class<?> maybeType) {
        return Types.parseBoolean(target);
    }

    @Override
    public String fromString(Object target) {
        return convert(target).toString();
    }

    @Override
    public Boolean defaultValue() {
        return Boolean.FALSE;
    }

    @Override
    public Class<?> getType() {
        return Types.BOOLEAN;
    }
}
