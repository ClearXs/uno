package cc.allio.uno.core.util;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * BigDecimal相关数据工具类
 *
 * @author jiangwei
 * @date 2022/6/17 20:05
 * @since 1.0
 */
@Slf4j
public class BigDecimalUtils {

    /**
     * 创建BigDecimal对象
     *
     * @param val Long类型数据
     * @return BigDecimal实例数据
     */
    public static BigDecimal creator(Long val) {
        return BigDecimal.valueOf(val);
    }

    /**
     * 创建BigDecimal对象
     *
     * @param val 字符串数据
     * @return BigDecimal实例数据或者null（有异常时抛出）
     */
    public static BigDecimal creator(String val) {
        try {
            return new BigDecimal(val);
        } catch (Throwable e) {
            log.error("Creator {} To BigDecimal Failed", val);
            return null;
        }
    }

    /**
     * 创建BigDecimal对象
     *
     * @param val Double数据
     * @return BigDecimal实例数据
     */
    public static BigDecimal creator(Double val) {
        return BigDecimal.valueOf(val);
    }

    /**
     * 创建BigDecimal对象
     *
     * @param val Integer数据
     * @return BigDecimal实例数据
     */
    public static BigDecimal creator(Integer val) {
        return new BigDecimal(val);
    }
}
