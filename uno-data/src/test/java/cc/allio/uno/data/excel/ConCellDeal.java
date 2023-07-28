package cc.allio.uno.data.excel;

import cc.allio.uno.core.util.DateUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.poi.ss.usermodel.Workbook;
import reactor.core.publisher.Flux;

import java.util.*;
import java.util.stream.Collectors;

public class ConCellDeal extends CollectionCelDeal {

    @Override
    public Workbook deal() {

        //
        List<?> list = Collections.emptyList();
        List<Map<String, Object>> bookList = super.getBookList(list);

        // ...
//        mapToList();
//        getBookList();

        return super.deal();
    }

    protected List<Map<String, Object>> mapToList(Map<String, Collection> conList) {
        // ...
//        conList.values().stream().flatMap(collection -> super.getBookList(collection).stream())
//                .collect(Collectors.groupingBy())
        // ...
        Date date = getDate(null);
        String key = "pres" + date;

        // ...
        return Collections.emptyList();
    }

    public static void main(String[] args) {
        Map<String, Collection<ValueTime>> conList = Maps.newHashMap();

        ValueTime valueTime1 = new ValueTime("1", DateUtil.parse("2022-11-11 10:10:10"));
        ValueTime valueTime2 = new ValueTime("1", DateUtil.parse("2022-10-11 10:10:10"));
        ValueTime valueTime3 = new ValueTime("2", DateUtil.parse("2022-10-13 10:10:10"));
        conList.put("2022-11", Lists.newArrayList(valueTime1));
        conList.put("2022-10", Lists.newArrayList(valueTime2, valueTime3));

        // key 2022-11
        // 1. List<Map<String, Object>

        Collection<Map<String, Object>> values = conList.entrySet().stream().flatMap(entry -> {
                    String key = entry.getKey();
                    return entry.getValue().stream().map(valueTime -> {
                                Map<String, Object> properties = Maps.newHashMap();
                                properties.put("mot", DateUtil.format(valueTime.getTime(), "dd HH:mm"));

                                properties.put(key + valueTime.getValue(), valueTime.getValue());
                                return properties;
                            });
                })
                .collect(Collectors.groupingBy(k -> k.get("mot"), Collectors.reducing(Maps.<String, Object>newHashMap(), (a, b) -> {
                    a.putAll(b);
                    return a;
                })))
                .values();

//        Flux.fromIterable(conList.entrySet())
//                .flatMap(entry -> {
//                    String key = entry.getKey();
//                    return Flux.fromIterable(entry.getStringValue())
//                            .map(valueTime -> {
//                                Map<String, Object> properties = Maps.newHashMap();
//                                properties.put("mot", DateUtil.format(valueTime.getTime(), "dd HH:mm"));
//
//                                properties.put(key + valueTime.getStringValue(), valueTime.getStringValue());
//                                return properties;
//                            });
//
//                })
//                .groupBy(k -> k.get("mot"))
//                .flatMap(g ->
//                        g.reduce(Maps.newHashMap(), (a, b) -> {
//                            a.putAll(b);
//                            return a;
//                        }))
//                .collectList()
//                .subscribe(list -> {
//                    System.out.println(list);
//                });



        //List<Map<String, Object>>
        List<HashMap<Object, Object>> list = Flux.fromIterable(conList.entrySet())
                .flatMap(entry -> {
                    String key = entry.getKey();
                    return Flux.fromIterable(entry.getValue())
                            .map(valueTime -> {
                                Map<String, Object> properties = Maps.newHashMap();
                                properties.put("mot", DateUtil.format(valueTime.getTime(), "dd HH:mm"));

                                properties.put(key + valueTime.getValue(), valueTime.getValue());
                                return properties;
                            });

                })
                .groupBy(k -> k.get("mot"))
                .flatMap(g ->
                        g.reduce(Maps.newHashMap(), (a, b) -> {
                            a.putAll(b);
                            return a;
                        }))
                .collectList()
                .block();

    }

    @Data
    @AllArgsConstructor
    static class ValueTime {

        private String value;
        private Date time;
    }
}
