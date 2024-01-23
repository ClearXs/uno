package cc.allio.uno.data.query.param;

import cc.allio.uno.data.query.stream.DiluentTimeStream;
import cc.allio.uno.data.query.stream.OutliersIgnoreTimeStream;
import cc.allio.uno.data.query.stream.SupplementTimeStream;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class QuerySetting {

    /**
     * 过滤异常值
     *
     * @see OutliersIgnoreTimeStream
     */
    private boolean filterOutliers = Boolean.FALSE;

    /**
     * 是否进行数据增补（增补是耗时操作，按照数据量其算法消耗也是一个量级操作，不默认开启）
     *
     * @see SupplementTimeStream
     */
    private boolean supplement = Boolean.FALSE;

    /**
     * 数据抽稀
     *
     * @see DiluentTimeStream
     */
    private DataDilute dataDilute;
}
