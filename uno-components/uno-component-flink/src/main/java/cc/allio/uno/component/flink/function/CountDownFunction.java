package cc.allio.uno.component.flink.function;

import org.apache.flink.configuration.Configuration;

/**
 * 锁存器Function
 *
 * @author jiangwei
 * @date 2022/2/24 00:49
 * @since 1.0
 */
@Deprecated
public interface CountDownFunction {

    /**
     * 子类实现初始化操作
     *
     * @param parameters The configuration containing the parameters attached to the contract.
     */
    void afterOpen(Configuration parameters);
}
