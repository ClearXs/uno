package cc.allio.uno.data.query.excel;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cc.allio.uno.data.query.param.DateDimension;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.*;

/**
 *历史监测数据的导出调用接口
 *
 * @author cxd
 * @date 2022/12/30 22:49
 * @since 1.1.2
 */
public class ValueTimeExcelWriter {


    /**
     * 根据入参构建导出表格，入参主要有是否同期，统计数据，统计字段
     * 添加数据（同期数据和非同期数据）
     */
    public static Workbook exportHisExcel(CellHeader cellHeader, Map<String, Collection<?>> dataMap){
        //生成表头
        DateDimension[] contemporaneous = cellHeader.getContemporaneous();
        List<ExcelExportEntity> colList = new ArrayList<>(cellHeader.getHeaders().values());
        //插入表数据
        CellDataDeal cellDataDeal = CellDataDealFactory.create(contemporaneous);
        List<Map<String, Object>> list = cellDataDeal.makeExcelData(dataMap,cellHeader);
        return ExcelExportUtil.exportExcel(new ExportParams(), colList, list);
    }


}
