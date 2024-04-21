package cc.allio.uno.data.test.executor.testcontainers;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.test.testcontainers.Container;
import cc.allio.uno.test.testcontainers.ContainerType;
import cc.allio.uno.test.testcontainers.Prelude;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.InfluxDBContainer;

/**
 * prepare influxdb in start before.
 *
 * @author j.x
 * @date 2024/4/16 14:13
 * @see cc.allio.uno.data.test.executor.translator.InfluxdbTranslator
 * @since 1.1.8
 */
@AutoService(Prelude.class)
public class InfluxdbPrelude implements Prelude {

    @Override
    public void onPrepare(Container container) {
        GenericContainer<?> internal = container.getInternal();
        if (internal instanceof InfluxDBContainer<?> influxDBContainer) {
            influxDBContainer.withAdminToken("admin");
        }
    }

    @Override
    public boolean match(ContainerType containerType) {
        return ContainerType.Influxdb == containerType;
    }
}
