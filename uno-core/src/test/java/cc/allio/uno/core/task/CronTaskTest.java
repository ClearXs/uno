package cc.allio.uno.core.task;

import cc.allio.uno.core.BaseTestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

@Slf4j
class CronTaskTest extends BaseTestCase {

    @Override
    protected void onInit() throws Throwable {

    }

    @Test
    void testPerMinute() throws ParseException {
        CronTask<String> task = new CronTask<>(CronTask.EXECUTE_PER_MINUTE);
        assertDoesNotThrow(() -> {
            task.addComputableTask((buffer, current) -> {
                log.info("Cron pre minute");
            });
            Thread.sleep(3000L);
        });
    }

    @Override
    protected void onDown() throws Throwable {

    }
}
