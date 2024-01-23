package cc.allio.uno.data.tx;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;

@Configuration
@AutoConfigureAfter({TransactionAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class})
@ConditionalOnClass(TransactionManager.class)
public class TxAutoConfiguration {

    @Bean
    public TransactionContext transactionContext(PlatformTransactionManager transactionManager) {
        return new TransactionContext(transactionManager);
    }

}
