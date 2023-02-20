package cc.allio.uno.component.sequential;

import cc.allio.uno.core.metadata.Metadata;
import cc.allio.uno.core.util.id.IdGenerator;
import cc.allio.uno.core.proxy.ComposeOrigin;
import cc.allio.uno.core.proxy.ComposeSharable;

import java.util.function.Predicate;

/**
 * 时序数据接口，模版方法（获取有关时序数据操作）。
 *
 * @author jw
 * @date 2021/12/13 14:10
 */
public interface Sequential extends Metadata {

    /**
     * 获取当前时序数据的类型
     *
     * @return 类型的字符串
     */
    @ComposeSharable
    String getType();

    /**
     * process-time
     *
     * @return timestamp时间
     */
    Long processTime();

    /**
     * event-time
     *
     * @return 默认返回处理时间
     */
    Long eventTime();

    /**
     * 当前时序数据与另外一个时序数据进行比较
     *
     * @return {@link Predicate}实例对象
     */
    Predicate<Sequential> selfChanged();

    // -------------------- DEFAULT METHOD --------------------

    /**
     * 获取时序数据原始类型（时序数据可能是代理类）
     *
     * @return 原始数据类型
     * @see BaseCompositeSequential
     * @see ComposeOrigin
     */
    default String getOriginType() {
        return getOriginType(this);
    }

    /**
     * 获取时序数据原始类型（时序数据可能是代理类）
     *
     * @param sequential 时序数据
     * @return 原始数据类型
     * @see BaseCompositeSequential
     * @see ComposeOrigin
     */
    default String getOriginType(@ComposeOrigin Sequential sequential) {
        return sequential.getType();
    }

    /**
     * 获取时序数据Id
     *
     * @return Long类型的Id，默认返回分布式Id
     */
    default Long getSeqId() {
        return IdGenerator.defaultGenerator().getNextId();
    }

    /**
     * 获取时序数据标识code
     *
     * @return 标识code，默认返回类型
     */
    default String getCode() {
        return getType();
    }

    /**
     * 判断当前Sequential对象是否存在计算属性实例对象
     *
     * @return 子类进行实现，返回断言对象
     */
    default boolean computed() {
        return false;
    }

}
