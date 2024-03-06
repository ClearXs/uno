package cc.allio.uno.core.task;

import cc.allio.uno.core.BaseTestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class TimerSegmentTaskBaseTest extends BaseTestCase {

    TimerSegmentTask<String> timeSegmentTask;

    @Override
    protected void onInit() throws Throwable {
        timeSegmentTask = new TimerSegmentTask<>(TimerSegmentTask.ONE_SECOND_ROUND);
    }

    @Test
    void testOneSecond() {
        assertDoesNotThrow(() -> {
            timeSegmentTask.addComputableTask((buffer, current) -> log.info("compute buffer: {}", buffer));
            Thread.sleep(2000);
        });
    }

    @Test
    void testException() {
        assertDoesNotThrow(() -> {
            timeSegmentTask.addComputableTask((buffer, current) -> {
                log.info("compute buffer: {}", buffer);
                throw new NullPointerException("");
            });
            Thread.sleep(30000);
        });
    }
}
