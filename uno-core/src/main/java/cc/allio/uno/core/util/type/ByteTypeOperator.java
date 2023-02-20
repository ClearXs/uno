package cc.allio.uno.core.util.type;

/**
 * Byte类型转换器，可能抛出NumberFormatException异常
 *
 * @author jiangwei
 * @date 2021/12/23 20:16
 * @since 1.0
 */
public class ByteTypeOperator extends UnsupportedCalculateOperator {

    @Override
    public Object convert(Object target, Class<?> maybeType) {
        return Byte.parseByte(target.toString());
    }

    @Override
    public String fromString(Object target) {
        return convert(target, Byte.class).toString();
    }

    @Override
    public Object defaultValue() {
        return Byte.valueOf("0");
    }
}
