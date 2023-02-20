package cc.allio.uno.data.orm.dialect.type;

import cc.allio.uno.core.util.type.Types;

import java.math.BigDecimal;

/**
 * big decimal java type
 *
 * @author jiangwei
 * @date 2023/1/13 18:16
 * @since 1.1.4
 */
public class BigDecimalJavaType extends JavaTypeImpl<BigDecimal> {

    @Override
    public Class<BigDecimal> getJavaType() {
        return Types.BIG_DECIMAL;
    }
}
