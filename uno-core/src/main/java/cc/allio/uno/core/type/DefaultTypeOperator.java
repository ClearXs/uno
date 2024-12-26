package cc.allio.uno.core.type;

/**
 * 默认转换器
 *
 * @author j.x
 * @since 1.0
 */
public class DefaultTypeOperator extends UnsupportedCalculateOperator<Object> {
    @Override
    public Object convert(Object target, Class<?> maybeType) {
        return target;
    }

    @Override
    public String fromString(Object target) {
        return convert(target).toString();
    }

    @Override
    public Class<?> getType() {
        return null;
    }
}
