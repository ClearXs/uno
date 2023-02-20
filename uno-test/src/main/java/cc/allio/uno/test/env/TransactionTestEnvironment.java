package cc.allio.uno.test.env;

import cc.allio.uno.test.BaseCoreTest;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;

/**
 * 事物环境
 *
 * @author jiangwei
 * @date 2022/2/14 14:23
 * @since 1.0
 */
public class TransactionTestEnvironment implements TestSpringEnvironment {
    @Override
    public void support(BaseCoreTest test) {
        test.registerComponent(TransactionAutoConfiguration.class);
        test.registerComponent(DataSourceTransactionManagerAutoConfiguration.class);
    }

    public static TransactionTestEnvironment getInstance() {
        return new TransactionTestEnvironment();
    }
}
