package cc.allio.uno.test.mock;

/**
 * 模拟接口返回数据，实现该接口的类一定存在一个无参构造器
 *
 * @author j.x
 */
@FunctionalInterface
public interface Mock<T> {

	/**
	 * 获取Mock的数据
	 *
	 * @return 返回模拟数据的实例
	 */
	T getData();
}
