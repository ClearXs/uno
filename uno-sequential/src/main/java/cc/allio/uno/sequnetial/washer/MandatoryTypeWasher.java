package cc.allio.uno.sequnetial.washer;

import cc.allio.uno.sequnetial.Sequential;

import java.util.function.Predicate;

/**
 * 不跳过类型检测
 *
 * @author j.x
 * @date 2022/5/20 15:36
 * @since 1.0
 */
public abstract class MandatoryTypeWasher implements FilterWasher {

    @Override
    public Predicate<Sequential> skip() {
        return sequential -> false;
    }
}
