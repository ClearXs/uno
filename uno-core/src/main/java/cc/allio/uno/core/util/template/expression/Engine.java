package cc.allio.uno.core.util.template.expression;

/**
 * 表达式替换引擎
 *
 * @author jiangwei
 * @date 2022/12/3 19:48
 * @since 1.1.2
 */
public interface Engine {

    /**
     * 运行该表达式引擎获取结果
     *
     * @return 结果
     * @throws Throwable 运行过程中出现错误时抛出
     */
    String run(String expression, Object value) throws Throwable;
}
