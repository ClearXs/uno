package cc.allio.uno.core.util.type;

import cc.allio.uno.core.util.DateUtil;
import org.springframework.util.ObjectUtils;

import java.text.ParseException;
import java.util.Date;

/**
 * Date类型转换器，以yyyy-MM-dd HH:mm:ss解析
 *
 * @author jiangwei
 * @date 2021/12/23 20:24
 * @since 1.0
 */
public class DateCalculateOperator extends UnsupportedCalculateOperator {

    @Override
    public Object convert(Object target, Class<?> maybeType) {
        try {
            if (target instanceof Date) {
                return target;
            }
            if (ObjectUtils.isEmpty(target)) {
                return defaultValue();
            } else {
                return DateUtil.DATETIME_FORMAT.parse(target.toString());
            }
        } catch (ParseException e) {
            throw new NullPointerException(e.getMessage());
        }
    }

    @Override
    public String fromString(Object target) {
        return DateUtil.formatDate((Date) convert(target, Date.class));
    }

    @Override
    public Object defaultValue() {
        return new Date();
    }
}
