package cc.allio.uno.data.query.stream;

import cc.allio.uno.core.bean.ValueWrapper;
import cc.allio.uno.core.util.DateUtil;
import cc.allio.uno.data.orm.dsl.OrderCondition;
import cc.allio.uno.data.orm.dsl.DSLException;

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
    default void setTimeData(String timeField, ValueWrapper incrementWrapper, Date date) {
        PropertyDescriptor propertyDescriptor = incrementWrapper.find(timeField);
        Class<?> timeType;
        Class<?> propertyType = propertyDescriptor.getPropertyType();
        Class<?> propertyEditorClass = propertyDescriptor.getPropertyEditorClass();
        if (propertyType == null) {
            timeType = propertyEditorClass;
        } else {
            timeType = propertyType;
        }
        if (timeType == null) {
            throw new IllegalArgumentException(
                    String.format("value instance %s can't expect time type", incrementWrapper.getTarget()));
        }
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
        if (maybeTime == null) {
            throw new IllegalArgumentException("the maybeTime is empty");
        }
        if (String.class.isAssignableFrom(maybeTime.getClass())) {
            return DateUtil.parse(maybeTime.toString());
        } else if (Date.class.isAssignableFrom(maybeTime.getClass())) {
            return (Date) maybeTime;
        } else if (Long.class.isAssignableFrom(maybeTime.getClass())) {
            return new Date((Long) maybeTime);
        }
        throw new DSLException("query datetime neither 'String' and not 'Date' and not 'Long'");
    }

}
