package cc.allio.uno.test;

/**
 * 基准测试
 *
 * @author jw
 * @date 2021/12/15 23:24
 */
public interface BenchmarkTest {

	/**
	 * 测试前初始化
	 */
	void setup();

	/**
	 * 测试完的后处理
	 */
	void tearDown();
}
