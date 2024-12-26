package cc.allio.uno.test;

/**
 * 基准测试
 *
 * @author j.x
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
