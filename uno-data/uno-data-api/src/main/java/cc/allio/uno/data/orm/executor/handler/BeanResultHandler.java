package cc.allio.uno.data.orm.executor.handler;

import cc.allio.uno.core.reflect.ReflectTools;

/**
 * marked bean handler interface
 *
 * @param <R> bean type
 * @author j.x
 * @since 1.1.7
 */
public interface BeanResultHandler<R> extends ResultHandler {

    /**
     * 获取bean 类型
     *
     * @return Class
     */
    default Class<R> getBeanType() {
        return (Class<R>) ReflectTools.getGenericType(this, BeanResultHandler.class);
    }
}
