package cc.allio.uno.core.util.type;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * BigDecimal类型转换器，可能抛出NumberFormatException异常
 *
 * @author jiangwei
 * @date 2021/12/23 20:18
 * @since 1.0
 */
public class BigDecimalTypeOperator implements TypeOperator {

    @Override
    public Object convert(Object target, Class<?> maybeType) {
        return new BigDecimal(target.toString());
    }

    @Override
    public int signum(Object target) {
        BigDecimal bigDecimal = new BigDecimal(target.toString());
        return bigDecimal.signum();
    }

    @Override
    public String fromString(Object target) {
        BigDecimal bigDecimal = new BigDecimal(target.toString());
        return bigDecimal.toString();
    }

    @Override
    public Object add(Object origin, Object passive) {
        return new BigDecimal(origin.toString())
                .add(new BigDecimal(passive.toString()))
                .setScale(2, RoundingMode.UP);
    }

    @Override
    public Object subtract(Object origin, Object passive) {
        return new BigDecimal(origin.toString())
                .subtract(new BigDecimal(passive.toString()))
                .setScale(2, RoundingMode.UP);
    }

    @Override
    public Object multiply(Object origin, Object passive) {
        return new BigDecimal(origin.toString())
                .multiply(new BigDecimal(origin.toString()))
                .setScale(2, RoundingMode.UP);
    }

    @Override
    public Object divide(Object origin, Object passive) {
        return new BigDecimal(origin.toString())
                .divide(new BigDecimal(passive.toString()))
                .setScale(2, RoundingMode.UP);
    }

    @Override
    public Object defaultValue() {
        return BigDecimal.ZERO;
    }
}
