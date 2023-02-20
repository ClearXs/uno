package cc.allio.uno.data.excel;

import cc.allio.uno.core.util.DateUtil;
import cc.allio.uno.data.query.excel.CellIndex;
import cc.allio.uno.data.query.param.DateDimension;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.*;

public class CollectionExcelTest {






    public static void main(String[] args) {
    //数据准备
        DateDimension[] contemporaneous;
        Map<String, Collection<?>> dataMap;
        String[] dataFields;
        String timeField;
        List<CellIndex> cellIndices;
        //同期参数
        contemporaneous = null;
        //流量数据
        dataMap=new HashMap<>();
        FlowHis flowHisOne = new FlowHis();
        flowHisOne.setMot(DateUtil.parse("2022-12-01 01:00:00"));
        flowHisOne.setWaterAmount(new BigDecimal(1));
        flowHisOne.setFlowAmount(new BigDecimal(1));
        FlowHis flowHisTwo = new FlowHis();
        flowHisTwo.setMot(DateUtil.parse("2022-12-02 02:00:00"));
        flowHisTwo.setWaterAmount(new BigDecimal(2));
        flowHisTwo.setFlowAmount(new BigDecimal(2));
        FlowHis flowHisThree = new FlowHis();
        flowHisThree.setMot(DateUtil.parse("2022-12-03 03:00:00"));
        flowHisThree.setWaterAmount(new BigDecimal(3));
        flowHisThree.setFlowAmount(new BigDecimal(3));
        dataMap.put("queryList", Arrays.asList(flowHisOne,flowHisTwo,flowHisThree));
        //监测数据字段
        dataFields=new String[2];
        dataFields[0]="waterAmount";
        dataFields[1]="flowAmount";
        //时间字段
        timeField="mot";
        //监测字段中文名
        cellIndices=new ArrayList<>();
        cellIndices.add(new CellIndex("waterAmount","累计流量"));
        cellIndices.add(new CellIndex("flowAmount","瞬时流量"));
    //表头测试
//        List<ExcelExportEntity> excelExportEntities = CellHeader.makeExcelHeader(contemporaneous, dataFields, timeField, cellIndices);
//        System.out.println(excelExportEntities);

    //表数据测试
//        CellDataDeal cellDataDeal = CellDataDealFactory.create(contemporaneous);
//        List<Map<String, Object>> list = cellDataDeal.makeExcelData(contemporaneous, dataMap, dataFields,excelExportEntities,timeField);
//        System.out.println(list);

    }




    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FlowHis{

        Date mot;

        BigDecimal waterAmount;

        BigDecimal flowAmount;

    }
}
