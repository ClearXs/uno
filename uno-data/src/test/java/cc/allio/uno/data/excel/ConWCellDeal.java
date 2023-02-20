package cc.allio.uno.data.excel;

import java.util.Date;

public class ConWCellDeal extends ConCellDeal {

    private final String format;
    public ConWCellDeal(String format) {
        this.format = format;
    }

    @Override
    protected Date getDate(Object maybeTime) {

        return super.getDate(maybeTime);
    }

    @Override
    protected String buildKey() {
        return "";
    }
}
