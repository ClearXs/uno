package cc.allio.uno.data.excel;

import cc.allio.uno.data.query.excel.CellDataDeal;
import cc.allio.uno.data.query.excel.CellDataDealFactory;
import cc.allio.uno.data.query.excel.CellHeader;
import cc.allio.uno.data.query.excel.CellIndex;
import cc.allio.uno.data.query.param.TimeDimension;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cc.allio.uno.core.util.DateUtil;
import cc.allio.uno.data.query.param.DateDimension;
import cc.allio.uno.test.BaseTestCase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

public class ContemporaneousExcelTest extends BaseTestCase {


    @Test
    void testContemporaneousExcel() {
        //数据准备
        DateDimension[] contemporaneous;
        Map<String, Collection<?>> dataMap;
        String[] dataFields;
        String timeField;
        List<CellIndex> cellIndices;
        //同期参数
        contemporaneous = new DateDimension[2];
        DateDimension dateDimensionOne = new DateDimension();
        dateDimensionOne.setDate("2022-12");
        dateDimensionOne.setDimension(TimeDimension.MONTH);
        contemporaneous[0] = dateDimensionOne;
        DateDimension dateDimensionTwo = new DateDimension();
        dateDimensionTwo.setDate("2022-11");
        dateDimensionTwo.setDimension(TimeDimension.MONTH);
        contemporaneous[1] = dateDimensionTwo;
        //流量数据
        dataMap = new HashMap<>();

        List<FlowHis> listDec = new ArrayList<>();
        List<FlowHis> listNov = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            FlowHis flowHisDec = new FlowHis();
            flowHisDec.setMot(DateUtil.plusMinutes(DateUtil.parse("2022-12-01"), i));
            flowHisDec.setWaterAmount(new BigDecimal(i));
            flowHisDec.setFlowAmount(new BigDecimal(i));
            listDec.add(flowHisDec);
            FlowHis flowHisNov = new FlowHis();
            flowHisNov.setMot(DateUtil.plusMinutes(DateUtil.parse("2022-11-03"), i));
            flowHisNov.setWaterAmount(new BigDecimal(i));
            flowHisNov.setFlowAmount(new BigDecimal(i));
            listNov.add(flowHisNov);
        }

        dataMap.put("2022-12", listDec);
        dataMap.put("2022-11", listNov);
        //监测数据字段
        dataFields = new String[2];
        dataFields[0] = "waterAmount";
        dataFields[1] = "flowAmount";
        //时间字段
        timeField = "mot";
        //监测字段中文名
        cellIndices = new ArrayList<>();
        cellIndices.add(new CellIndex("waterAmount", "累计流量"));
        cellIndices.add(new CellIndex("flowAmount", "瞬时流量"));
        //表头测试
        CellHeader cellHeader = new CellHeader(contemporaneous, dataFields, timeField, cellIndices);

        List<ExcelExportEntity> excelExportEntities = new ArrayList<>(cellHeader.getHeaders().values());

        //表数据测试
        timing();
        CellDataDeal cellDataDeal = CellDataDealFactory.create(contemporaneous);
        List<Map<String, Object>> list = cellDataDeal.makeExcelData( dataMap, cellHeader);
        System.out.println();
        stopTiming();
    }

    @Test
    void testMapValueToList() {
        Map<Long, Double> highLowValueMap=new HashMap<Long, Double>();
        highLowValueMap.put(1l, 10.0);
        highLowValueMap.put(2l, 20.0);
        List<Double> valuesToMatch1  = new ArrayList<Double>( highLowValueMap.values());
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FlowHis {

        Date mot;

        BigDecimal waterAmount;

        BigDecimal flowAmount;

    }
}
