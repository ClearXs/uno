package cc.allio.uno.data.orm.dsl.helper;

/**
 * POJO相关的检查器
 *
 * @author j.x
 * @since 1.1.7
 */
public interface PojoInspect {

    /**
     * 检查是否是 POJO
     *
     * @param maybePojo maybePojo
     * @return true if pojo
     */
    boolean isPojo(Class<?> maybePojo);

    /**
     * 是否使用缓存
     */
    boolean useCache();
}
