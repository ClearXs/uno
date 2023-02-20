package cc.allio.uno.data.excel;

import org.apache.poi.ss.usermodel.Workbook;

import java.util.*;

public class CollectionCelDeal implements CellDeal{


    public CollectionCelDeal() {

    }

    @Override
    public Workbook deal() {
//        getBookList()
        return null;
    }

    protected List<Map<String, Object>> getBookList(Collection<?> collection) {
        // ...

        // ...
        Date date = getDate(null);
        String key = "pers";

        // ...
        return Collections.emptyList();
    }

    protected Date getDate(Object maybeTime) {
        return new Date();
    }

    protected String buildKey() {
        return "pres";
    }
}
