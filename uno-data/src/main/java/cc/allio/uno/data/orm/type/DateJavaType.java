package cc.allio.uno.data.orm.type;

import cc.allio.uno.core.type.Types;

import java.util.Date;

/**
 * date java type
 *
 * @author jiangwei
 * @date 2023/4/14 18:49
 * @since 1.1.4
 */
public class DateJavaType extends JavaTypeImpl<Date> {
    @Override
    public Class<Date> getJavaType() {
        return Types.DATE;
    }
}
