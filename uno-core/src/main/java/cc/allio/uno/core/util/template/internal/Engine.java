package cc.allio.uno.core.util.template.internal;

/**
 * 表达式替换引擎
 *
 * @author j.x
 * @date 2022/12/3 19:48
 * @since 1.1.2
 */
public interface Engine {

    /**
     * 运行该表达式引擎获取结果
     *
     * @param expression 原表达式
     * @param value      赋予值
     * @param langsym    是否保留语言符合，如 true = "2" false = 2
     * @return 结果
     * @throws Throwable 运行过程中出现错误时抛出
     */
    String run(String expression, Object value, boolean langsym);
}
