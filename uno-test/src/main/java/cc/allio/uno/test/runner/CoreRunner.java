package cc.allio.uno.test.runner;

import lombok.Data;

import java.util.Collection;

/**
 * 存放不同类型的Runner
 *
 * @author jiangwei
 * @date 2023/3/3 16:18
 * @since 1.1.4
 */
@Data
public class CoreRunner {

    private final Collection<Runner> registerRunner;
    private final Collection<Runner> refreshCompleteRunner;
    private final Collection<Runner> closeRunner;

    public CoreRunner(Collection<Runner> registerRunner,
                      Collection<Runner> refreshCompleteRunner,
                      Collection<Runner> closeRunner) {
        this.registerRunner = registerRunner;
        this.refreshCompleteRunner = refreshCompleteRunner;
        this.closeRunner = closeRunner;
    }
}
