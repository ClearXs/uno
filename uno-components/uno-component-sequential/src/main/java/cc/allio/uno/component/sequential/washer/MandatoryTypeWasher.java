package cc.allio.uno.component.sequential.washer;

import cc.allio.uno.component.sequential.Sequential;

import java.util.function.Predicate;

/**
 * 不跳过类型检测
 *
 * @author jiangwei
 * @date 2022/5/20 15:36
 * @since 1.0
 */
public abstract class MandatoryTypeWasher implements FilterWasher {

    @Override
    public Predicate<Sequential> skip() {
        return sequential -> false;
    }
}
