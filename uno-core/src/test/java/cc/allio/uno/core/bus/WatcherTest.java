package cc.allio.uno.core.bus;

import cc.allio.uno.core.BaseTestCase;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WatcherTest extends BaseTestCase {

    String path = "1";

    @Test
    void testWatcherUntilComplete() {
        EventBusFactory.current().subscribe(path).subscribe();

        Watchers.watch(path)
                .untilComplete()
                .whenTrigger(() -> EventBusFactory.current().publish(path, new DefaultEventContext()).subscribe())
                .executable(() -> assertTrue(1 == 1))
                .doOn();

        Executors.newScheduledThreadPool(1)
                .schedule(() -> EventBusFactory.current().publish(path, new DefaultEventContext()).subscribe(), 100, TimeUnit.MILLISECONDS);

        Watchers.watch(path).doUntilComplete(() -> assertTrue(1 == 1));
    }
}
