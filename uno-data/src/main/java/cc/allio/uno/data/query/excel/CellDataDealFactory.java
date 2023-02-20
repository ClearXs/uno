package cc.allio.uno.data.query.excel;

import cc.allio.uno.data.query.param.DateDimension;

public class CellDataDealFactory {

    public static CellDataDeal create(DateDimension[] contemporaneous) {
        if (null != contemporaneous && contemporaneous.length > 0) {
            return new ContemporaneousCellDataDeal();
        } else {
            return new CollectionCellDataDeal();
        }
    }
}
