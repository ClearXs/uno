package cc.allio.uno.data.query.excel;



import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 *实现设备历史监测数据进行导出时候，调用该类方法进行数据处理
 *
 * @author cxd
 * @date 2022/12/30 22:49
 * @since 1.1.2
 */

public interface CellDataDeal {

    /**
     *
     * @param dataMap
     * @param cellHeader
     * @return
     */
    List<Map<String, Object>> makeExcelData(Map<String, Collection<?>> dataMap,CellHeader cellHeader);

}
