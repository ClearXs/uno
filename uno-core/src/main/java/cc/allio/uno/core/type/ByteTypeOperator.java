package cc.allio.uno.core.type;

/**
 * Byte类型转换器，可能抛出NumberFormatException异常
 *
 * @author j.x
 * @since 1.0
 */
public class ByteTypeOperator extends UnsupportedCalculateOperator<Byte> {

    @Override
    public Byte convert(Object target, Class<?> maybeType) {
        return Types.parseByte(target);
    }

    @Override
    public String fromString(Object target) {
        return convert(target).toString();
    }

    @Override
    public Byte defaultValue() {
        return Byte.valueOf("0");
    }

    @Override
    public Class<?> getType() {
        return Types.BYTE;
    }
}
