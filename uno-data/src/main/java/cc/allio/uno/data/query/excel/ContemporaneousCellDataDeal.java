package cc.allio.uno.data.query.excel;


import cc.allio.uno.core.bean.ObjectWrapper;
import cc.allio.uno.core.util.DateUtil;
import cc.allio.uno.data.query.param.DateDimension;
import com.google.common.collect.Maps;

import java.util.*;
import java.util.stream.Collectors;

/**
 *实现设备历史监测数据进行导出时候，调用该类方法进行{同期的}数据处理
 *
 * @author cxd
 * @date 2022/12/30 22:49
 * @since 1.1.2
 */

public class ContemporaneousCellDataDeal extends CollectionCellDataDeal {


    @Override
    public List<Map<String, Object>> makeExcelData(Map<String, Collection<?>> dataMap,CellHeader cellHeader) {
        String timeField = cellHeader.getTimeField();
        DateDimension[] contemporaneous = cellHeader.getContemporaneous();
        String[] dataField = cellHeader.getDataField();
        //同期数据
        return dataMap.entrySet().stream().flatMap(entry ->
                        entry.getValue().stream().map(dataDatum -> {
                            ObjectWrapper objectWrapper = new ObjectWrapper(dataDatum);
                            Object maybeTime = objectWrapper.getForce(timeField);
                            Map<String, Object> valMap = Maps.newHashMap();
                            Date dateTime = null;
                            if (maybeTime.getClass().isAssignableFrom(String.class)) {
                                dateTime = DateUtil.parse(maybeTime.toString());
                            } else if (maybeTime.getClass().isAssignableFrom(Date.class)) {
                                dateTime = (Date) maybeTime;
                            }
                            valMap.put(timeField, DateUtil.format(dateTime, TimeFormatDeal.getTimeFieldFormat(contemporaneous[0].getDimension())));
                            String timeHeadFormat = TimeFormatDeal.getTimeHeadFormat(contemporaneous[0].getDimension());
                            for (int i = 0; i < dataField.length; i++) {
                                valMap.put(dataField[i] + DateUtil.format(dateTime, timeHeadFormat), objectWrapper.getForce(dataField[i]));
                            }
                            return valMap;
                        }))
                .collect(Collectors.groupingBy(k -> k.get(timeField)))
                .values()
                .stream()
                .map(g -> g.stream().reduce(Maps.newHashMap(), (a, b) -> {
                    a.putAll(b);
                    return a;
                }))
                .sorted(new Comparator<Map<String, Object>>() {
                    @Override
                    public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                        Date time = DateUtil.parse((String) o2.get(timeField), TimeFormatDeal.getTimeFieldFormat(contemporaneous[0].getDimension()));
                        Date timeCom = DateUtil.parse((String) o1.get(timeField), TimeFormatDeal.getTimeFieldFormat(contemporaneous[0].getDimension()));
                        return time.compareTo(timeCom);
                    }
                })
                .collect(Collectors.toList());

    }


}

