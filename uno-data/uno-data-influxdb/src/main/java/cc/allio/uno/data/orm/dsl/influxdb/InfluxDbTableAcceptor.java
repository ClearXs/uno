package cc.allio.uno.data.orm.dsl.influxdb;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.data.orm.dsl.MetaAcceptor;
import cc.allio.uno.data.orm.dsl.Table;

/**
 * in influxdb {@link Table#getSchema()} is empty, but for compatible other data service. so this {@link MetaAcceptor} solution problem
 *
 * @author j.x
 * @date 2024/4/20 19:30
 * @since 1.1.8
 */
public class InfluxDbTableAcceptor implements MetaAcceptor<Table> {

    @Override
    public void onAccept(Table table) {
        table.setSchema(StringPool.EMPTY);
    }
}
