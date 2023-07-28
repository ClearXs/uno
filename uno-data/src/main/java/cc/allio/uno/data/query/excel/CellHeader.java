package cc.allio.uno.data.query.excel;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cc.allio.uno.data.query.param.DateDimension;
import com.google.common.collect.Maps;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 实现设备历史监测数据进行导出时候，调用该类方法进行导出表格表头的生成
 *
 * @author cxd
 * @date 2022/12/30 22:49
 * @since 1.1.2
 */
@Data
public class CellHeader {

    final Map<String, ExcelExportEntity> headers;
    final String timeField;
    final String[] dataField;
    final DateDimension[] contemporaneous;

    public CellHeader(DateDimension[] contemporaneous, String[] dataField, String timeField, List<CellIndex> cellIndices) {
        this.headers = Maps.newLinkedHashMap();
        Map<String, String> cellIndicesMap = cellIndices.stream().collect(Collectors.toMap(CellIndex::getKey, CellIndex::getName));
        ExcelExportEntity colEntity = new ExcelExportEntity("采集时间", timeField);
        colEntity.setNeedMerge(true);
        colEntity.setWidth(20.00);
        headers.put((String) colEntity.getKey(), colEntity);
        if (null != contemporaneous && contemporaneous.length > 0) {
            //同期数据
            for (int i = 0; i < dataField.length; i++) {
                for (int i1 = 0; i1 < contemporaneous.length; i1++) {
                    colEntity = new ExcelExportEntity(cellIndicesMap.get(dataField[i]) + contemporaneous[i1].getDate(), dataField[i] + contemporaneous[i1].getDate());
                    colEntity.setNeedMerge(true);
                    colEntity.setWidth(30.00);
                    headers.put((String) colEntity.getKey(), colEntity);
                }
            }
        } else {
            //非同期数据
            for (int i = 0; i < dataField.length; i++) {
                colEntity = new ExcelExportEntity(cellIndicesMap.get(dataField[i]), dataField[i]);
                colEntity.setWidth(20.00);
                colEntity.setNeedMerge(true);
                headers.put((String) colEntity.getKey(), colEntity);
            }
        }
        this.timeField = timeField;
        this.dataField = dataField;
        this.contemporaneous = contemporaneous;
    }


    public ExcelExportEntity get(String field) {
        return headers.get(field);
    }
}
