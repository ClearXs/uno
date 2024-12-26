package cc.allio.uno.data.orm.dsl.type;

import cc.allio.uno.core.type.Types;

import java.util.Queue;

/**
 * queue
 *
 * @author j.x
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
