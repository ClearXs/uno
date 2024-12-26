package cc.allio.uno.data.query.stream;

import cc.allio.uno.core.bean.ValueWrapper;
import cc.allio.uno.core.type.Types;
import cc.allio.uno.data.query.QueryFilter;
import cc.allio.uno.data.query.QueryWrapper;
import cc.allio.uno.data.query.param.QuerySetting;
import reactor.core.publisher.Flux;

/**
 * 时间序列数据异常值过滤。
 * <p>对于给定的查询数据字段{@link QueryWrapper#getDataFields()}与{@link QueryWrapper#getTimeField()}过滤针对null值，相同时间序列数据进行过滤</p>
 *
 * @author j.x
 * @since 1.1.0
 */
public class OutliersIgnoreTimeStream<T> extends FunctionalityTimeStream<T> {

    public OutliersIgnoreTimeStream(CollectionTimeStream<T> ds) {
        super(ds);
    }

    @Override
    protected boolean onFilter(QueryFilter queryFilter) {
        QuerySetting querySetting = queryFilter.getQueryWrapper().getQuerySetting();
        return querySetting != null && querySetting.isFilterOutliers();
    }

    @Override
    protected Flux<T> doRead(QueryFilter queryFilter, Flux<T> source) {
        QueryWrapper queryWrapper = queryFilter.getQueryWrapper();
        String[] dataFields = queryWrapper.getDataFields();
        String timeField = queryWrapper.getTimeField();
        return source
                // 空数据和负值过滤
                .filter(o -> {
                    ValueWrapper wrapper = ValueWrapper.get(o);
                    boolean dataNotEmpty = true;
                    for (String dataField : dataFields) {
                        Object value = wrapper.getForce(dataField);
                        try {
                            if (value == null || Types.signum(value) == -1) {
                                dataNotEmpty = false;
                            }
                        } catch (Throwable ex) {
                            // ignore
                        }
                    }
                    boolean timeNotEmpty = wrapper.getForce(timeField) != null;
                    return dataNotEmpty && timeNotEmpty;
                });
    }

}
