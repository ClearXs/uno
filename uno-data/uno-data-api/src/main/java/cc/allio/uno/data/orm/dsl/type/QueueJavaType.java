package cc.allio.uno.data.orm.dsl.type;

import cc.allio.uno.core.type.Types;

import java.util.Queue;

/**
 * queue
 *
 * @author j.x
 * @date 2023/4/16 17:10
 * @since 1.1.4
 */
public class QueueJavaType extends JavaTypeImpl<Queue> {
    @Override
    public Class<Queue> getJavaType() {
        return Types.QUEUE;
    }

    @Override
    public boolean equalsTo(Class<?> otherJavaType) {
        return Queue.class.isAssignableFrom(otherJavaType);
    }
}
