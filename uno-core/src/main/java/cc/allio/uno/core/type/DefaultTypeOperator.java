package cc.allio.uno.core.type;

/**
 * 默认转换器
 *
 * @author jiangwei
 * @date 2021/12/23 20:32
 * @since 1.0
 */
public class DefaultTypeOperator extends UnsupportedCalculateOperator {
    @Override
    public Object convert(Object target, Class<?> maybeType) {
        return target;
    }

    @Override
    public String fromString(Object target) {
        return convert(target, Object.class).toString();
    }

    @Override
    public Class<?> getType() {
        return null;
    }
}
