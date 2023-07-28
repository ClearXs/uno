package cc.allio.uno.data.orm.type;

import cc.allio.uno.core.type.Types;

import java.util.Queue;

/**
 * queue
 *
 * @author jiangwei
 * @date 2023/4/16 17:10
 * @since 1.1.4
 */
public class QueueJavaType extends JavaTypeImpl<Queue> {
    @Override
    public Class<Queue> getJavaType() {
        return Types.QUEUE;
    }
}
