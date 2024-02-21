package cc.allio.uno.data.orm.executor.handler;

import cc.allio.uno.core.util.ReflectTool;

/**
 * marked bean handler interface
 *
 * @param <R> bean type
 * @author jiangwei
 * @date 2024/2/14 16:52
 * @since 1.1.6
 */
public interface BeanResultHandler<R> extends ResultHandler {

    /**
     * 获取bean 类型
     *
     * @return Class
     */
    default Class<R> getBeanType() {
        return (Class<R>) ReflectTool.getGenericType(this, BeanResultHandler.class);
    }
}
