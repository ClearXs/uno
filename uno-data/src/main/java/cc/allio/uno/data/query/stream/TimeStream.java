package cc.allio.uno.data.query.stream;

import cc.allio.uno.core.bean.ObjectWrapper;
import cc.allio.uno.core.util.DateUtil;
import cc.allio.uno.data.sql.query.OrderCondition;

import java.beans.PropertyDescriptor;
import java.util.Date;

import static cc.allio.uno.core.util.DateUtil.PATTERN_HOUR;

/**
 * 标识当前为时间流数据，提供静态方法。
 *
 * @author jiangwei
 * @date 2022/11/17 11:11
 * @since 1.1.0
 */
public interface TimeStream<T> extends DataStream<T> {

    /**
     * 获取当前时刻按照时间间隔的下一步时间
     *
     * @param currentDate 当前时刻
     * @param timeSpacing 时间间隔
     * @return next step date
     */
    default Date nextStepDate(Date currentDate, long timeSpacing) {
        return nextStepDate(currentDate, timeSpacing, OrderCondition.DESC);
    }

    /**
     * 获取当前时刻按照时间间隔的下一步时间
     *
     * @param currentDate 当前时刻
     * @param timeSpacing 时间间隔
     * @param condition   增减量时间标识
     * @return next step date
     */
    default Date nextStepDate(Date currentDate, long timeSpacing, OrderCondition condition) {
        Date nextstep = new Date(currentDate.getTime());
        // 按照生降序增加时间间隔
        if (OrderCondition.DESC == condition) {
            nextstep.setTime(nextstep.getTime() - timeSpacing);
        } else if (OrderCondition.ASC == condition) {
            nextstep.setTime(nextstep.getTime() + timeSpacing);
        }
        return nextstep;
    }

    /**
     * 设置实体时间
     *
     * @param incrementWrapper 实体装饰器
     * @param timeField        时间字段
     */
    default void setTimeData(String timeField, ObjectWrapper incrementWrapper, Date date) {
        PropertyDescriptor propertyDescriptor = incrementWrapper.find(timeField);
        Class<?> timeType = propertyDescriptor.getPropertyType();
        if (timeType.isAssignableFrom(String.class)) {
            incrementWrapper.setForceCoverage(timeField, true, DateUtil.format(date, PATTERN_HOUR));
        } else if (timeType.isAssignableFrom(Date.class)) {
            incrementWrapper.setForceCoverage(timeField, true, date);
        } else {
            throw new IllegalArgumentException(
                    String.format("class %s expect time type is 'string' or 'date', but give is %s", incrementWrapper.getTarget().getClass(), propertyDescriptor.getPropertyType().getName()));
        }
    }


    /**
     * 数据时间
     *
     * @param maybeTime 可能产生时间
     * @return
     */
    default Date dateTime(Object maybeTime) {
        if (maybeTime.getClass().isAssignableFrom(String.class)) {
            return DateUtil.parse(maybeTime.toString());
        } else if (maybeTime.getClass().isAssignableFrom(Date.class)) {
            return (Date) maybeTime;
        }
        throw new RuntimeException("query datetime neither 'String' and not Date");
    }

}
