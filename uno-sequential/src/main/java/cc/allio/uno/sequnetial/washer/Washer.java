package cc.allio.uno.sequnetial.washer;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.type.Type;
import cc.allio.uno.sequnetial.context.SequentialContext;

import java.util.function.Predicate;

/**
 * 数据清洗器，子类必须有一个无参构造器
 *
 * @author j.x
 * @since 1.0
 */
public interface Washer {

    /**
     * 最高优先级
     */
    Integer MAX_PRIORITY = Integer.MIN_VALUE;

    /**
     * 最低优先级
     */
    Integer MIN_PRIORITY = Integer.MAX_VALUE;

    /**
     * 默认优先级
     */
    Integer DEFAULT_PRIORITY = 0;

    /**
     * 触发清洗操作
     *
     * @return 断言，true不需要清除数据，即是干净的数据，否则是脏数据
     */
    Predicate<SequentialContext> cleaning();

    /**
     * 清洗顺序，数值越低，越优先清洗，当靠前面的进行判定之后，如果不是干净的数据将不会在进行后续的判断
     *
     * @return 优先级整数
     * @see #MAX_PRIORITY
     * @see #MIN_PRIORITY
     * @see #DEFAULT_PRIORITY
     */
    int order();

    /**
     * 获取类型
     *
     * @return Type实例
     */
    Type getType();

    /**
     * 描述清洗器
     */
    default String description() {
        return StringPool.EMPTY;
    }
}
