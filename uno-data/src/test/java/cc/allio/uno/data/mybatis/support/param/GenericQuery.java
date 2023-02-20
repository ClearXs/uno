package cc.allio.uno.data.mybatis.support.param;

import cc.allio.uno.data.query.QueryFilter;
import cc.allio.uno.data.query.QueryWrapper;
import cc.allio.uno.data.query.param.DateDimension;
import cc.allio.uno.data.query.param.QuerySetting;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Date;

@Data
@Accessors(chain = true)
public class GenericQuery implements QueryWrapper {

    /**
     * 查询字段
     */
    private String[] dataColumns;

    /**
     * 时间字段
     */
    private String timeColumn;

    /**
     * 同期对比
     */
    private DateDimension[] contemporaneous;

    /**
     * 测站id
     */
    private Long stId;

    /**
     * 测站id
     */
    private String stcd;

    /**
     * 测站类型
     */
    private String sttp;

    /**
     * 查询开始时间
     */
    private Date startTime;

    /**
     * 查询结束时间
     */
    private Date endTime;

    /**
     * 查询设置
     */
    private QuerySetting setting;

    @Override
    public String[] getDataFields() {
        return dataColumns;
    }

    @Override
    public QuerySetting getQuerySetting() {
        return setting;
    }

    @Override
    public String getTimeField() {
        return timeColumn;
    }

    @Override
    public QueryFilter build() {
        QueryFilter query = new QueryFilter();

        if (ArrayUtils.isNotEmpty(getDataColumns())) {
            query.selectSql().select(getDataColumns());
        }
        if (StringUtils.isNotEmpty(getTimeField())) {
            query.selectSql().select(getTimeField());
        }
        // 过滤异常值
        if (getSetting() != null && Boolean.TRUE.equals(getSetting().isFilterOutliers())) {
            Arrays.stream(getDataColumns()).forEach(column -> query.whereSql().notNull(column));
        }
        // 时间
        if (getStartTime() != null && getEndTime() != null) {
            query.whereSql()
                    .gte("mot", startTime)
                    .lte("mot", endTime);
        }
        // 数据
        if (StringUtils.isNotEmpty(getSttp())) {
            query.whereSql().eq("sttp", getSttp());
        }
        if (StringUtils.isNotEmpty(stcd)) {
            query.whereSql().eq("stcd", stcd);
        }

        if (getStId() != null && getStId() > 0L) {
            query.whereSql().eq("stId", getStId());
        }
        query.orderSql().byDesc("mot");
        query.addQueryWrapper(this);
        return query;
    }

}
