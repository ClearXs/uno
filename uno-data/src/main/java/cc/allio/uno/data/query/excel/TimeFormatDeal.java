package cc.allio.uno.data.query.excel;

import cc.allio.uno.core.util.DateUtil;
import cc.allio.uno.data.query.param.TimeDimension;

/**
 * 导出表头和表数据的时间格式处理类
 *
 * @author cxd
 * @date 2022/12/30 22:49
 * @since 1.1.2
 */
public class TimeFormatDeal {

    public static String getTimeFieldFormat(TimeDimension timeDimension) {
        if (null != timeDimension) {
            if (TimeDimension.MONTH.equals(timeDimension)) {
                return "dd HH:mm";
            } else if (TimeDimension.DAY.equals(timeDimension)) {
                return "HH:mm";
            } else {
                throw new IllegalArgumentException("请输入合法参数");
            }
        } else {
            return "MM-dd HH:mm";
        }
    }

    public static String getTimeHeadFormat(TimeDimension timeDimension) {
        if (TimeDimension.MONTH.equals(timeDimension)) {
            return DateUtil.PATTERN_MONTH;
        } else if (TimeDimension.DAY.equals(timeDimension)) {
            return DateUtil.PATTERN_DATE;
        } else {
            throw new IllegalArgumentException("请输入合法参数");
        }
    }

}
