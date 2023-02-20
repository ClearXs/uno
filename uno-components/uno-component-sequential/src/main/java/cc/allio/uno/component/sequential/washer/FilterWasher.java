package cc.allio.uno.component.sequential.washer;

import cc.allio.uno.component.sequential.Sequential;

import java.util.function.Predicate;

/**
 * 数据、类型过滤清洁器
 *
 * @author jiangwei
 * @date 2022/5/20 15:38
 * @since 1.0
 */
public interface FilterWasher {

    /**
     * 根据类型做清洗
     *
     * @return 断言对象，true为通过，否则不通过
     */
    default Predicate<Sequential> filter() {
        return skip()
                .or(filter0()) // skip = false
                .and(sequential -> true); // skip = true
    }

    /**
     * 实现类进行实现具体的过滤操作
     *
     * @return 断言实例对象
     */
    Predicate<Sequential> filter0();

    /**
     * 是否跳过当前清洗
     *
     * @return 断言对象
     */
    Predicate<Sequential> skip();
}
