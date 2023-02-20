package cc.allio.uno.core.path;

/**
 * WebSocket推送路径管理器
 *
 * @author jiangwei
 * @date 2022/6/27 15:52
 * @since 1.0
 */
public class PathManager {

	private static Forest<String> forest = Forest.createRoot();

	/**
	 * 添加
	 *
	 * @param path 路径 /path/path1
	 * @see Forest#append(String)
	 */
	public static synchronized void push(String path) {
		forest.append(path);
	}

	/**
	 * 如果不存在则放入forest中
	 *
	 * @param path 路径
	 */
	public static synchronized void computeIfAbsent(String path) {
		boolean match = equalsPath(path);
		if (!match) {
			push(path);
		}
	}

	/**
	 * 判断是否存在于森林中
	 *
	 * @param path 路径集合
	 * @return true存在
	 */
	public static boolean equalsPath(String path) {
		return forest.getTopic(path).isPresent();
	}


	/**
	 * 返回当前森林数据
	 *
	 * @return
	 */
	public static Forest<String> toForest() {
		return forest;
	}


}
