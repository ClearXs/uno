package cc.allio.uno.data.query.excel;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cc.allio.uno.core.bean.ValueWrapper;
import cc.allio.uno.core.util.DateUtil;

import java.util.*;

/**
 * 实现设备历史监测数据进行导出时候，调用该类方法进行{非同期的}数据处理
 *
 * @author cxd
 * @date 2022/12/30 22:49
 * @since 1.1.2
 */
public class CollectionCellDataDeal implements CellDataDeal {

    @Override
    public List<Map<String, Object>> makeExcelData(Map<String, Collection<?>> dataMap, CellHeader cellHeader) {
        List<ExcelExportEntity> colList = new ArrayList<>(cellHeader.getHeaders().values());
        String timeField = cellHeader.getTimeField();
        //非同期数据
        List<Map<String, Object>> list = new ArrayList<>();
        Collection<?> dataList = dataMap.values().stream().findFirst().orElse(new ArrayList<>());
        for (Object dataDatum : dataList) {
            ValueWrapper valueWrapper = ValueWrapper.get(dataDatum);
            Map<String, Object> valMap = new HashMap<>();
            for (ExcelExportEntity excelExportEntity : colList) {
                if (timeField.equals(excelExportEntity.getKey())) {
                    Object maybeTime = valueWrapper.getForce(timeField);
                    Date dateTime = null;
                    if (maybeTime.getClass().isAssignableFrom(String.class)) {
                        dateTime = DateUtil.parse(maybeTime.toString());
                    } else if (maybeTime.getClass().isAssignableFrom(Date.class)) {
                        dateTime = (Date) maybeTime;
                    }
                    valMap.put(timeField, DateUtil.format(dateTime, TimeFormatDeal.getTimeFieldFormat(null)));
                } else {
                    valMap.put((String) excelExportEntity.getKey(), valueWrapper.getForce((String) excelExportEntity.getKey()));
                }
            }
            list.add(valMap);
        }
        return list;
    }
}
