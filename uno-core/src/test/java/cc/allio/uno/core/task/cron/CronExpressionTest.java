package cc.allio.uno.core.task.cron;

import cc.allio.uno.core.task.CronExpression;
import cc.allio.uno.core.task.CronTask;
import cc.allio.uno.core.BaseTestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Date;

@Slf4j
class CronExpressionTest extends BaseTestCase {

	@Override
	protected void onInit() throws Throwable {

	}

	@Test
	void testExecutePreSecond() throws ParseException {
		// 0/1 * * * * ? 每秒执行
		CronExpression executePreSecond = new CronExpression(CronTask.EXECUTE_PER_SECOND);
		Date now = new Date();
		Date endTime = executePreSecond.getTimeAfter(now);
		assertDoesNotThrow(() -> {
			log.info(String.valueOf(endTime.getTime() - now.getTime()));
		});
	}

	@Test
	void testExecutePreMinute() throws ParseException {
		// 0 0/1 * * * ? 每分钟执行
		CronExpression executePreMinute = new CronExpression(CronTask.EXECUTE_PER_MINUTE);
		Date now = new Date();
		Date endTime = executePreMinute.getTimeAfter(now);
		assertDoesNotThrow(() -> {
			log.info(String.valueOf(endTime.getTime() - now.getTime()));
		});
	}

	@Test
	void testExecutePreHour() throws ParseException {
		// 0 0/1 * * * ? 每小时执行
		CronExpression executePreHour = new CronExpression(CronTask.EXECUTE_PER_HOUR);
		Date now = new Date();
		Date endTime = executePreHour.getTimeAfter(now);
		assertDoesNotThrow(() -> {
			log.info(String.valueOf(endTime.getTime() - now.getTime()));
		});
	}

	@Test
	void testExecutePreDay() throws ParseException {
		// 0 0 0 1/1 * ? 每天执行
		CronExpression executePreDay = new CronExpression(CronTask.EXECUTE_PER_DAY);
		Date now = new Date();
		Date endTime = executePreDay.getTimeAfter(now);
		assertDoesNotThrow(() -> {
			log.info(String.valueOf(endTime.getTime() - now.getTime()));
		});
	}

	@Test
	void testExecutePreMonth() throws ParseException {
		// 0 0 0 L * ? 每月执行
		CronExpression executePreMonth = new CronExpression(CronTask.EXECUTE_PER_MONTH);
		Date now = new Date();
		Date endTime = executePreMonth.getTimeAfter(now);
		assertDoesNotThrow(() -> {
			log.info(String.valueOf(endTime.getTime() - now.getTime()));
		});
	}

	@Override
	protected void onDown() throws Throwable {

	}
}
