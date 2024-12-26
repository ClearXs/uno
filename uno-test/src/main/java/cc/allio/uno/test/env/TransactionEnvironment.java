package cc.allio.uno.test.env;

import cc.allio.uno.test.env.annotation.properties.TransactionProperties;
import cc.allio.uno.test.CoreTest;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;

import java.lang.annotation.Annotation;

/**
 * 事物环境
 *
 * @author j.x
 * @since 1.0
 */
public class TransactionEnvironment extends VisitorEnvironment {

    @Override
    protected void onSupport(CoreTest coreTest) throws Throwable {
        coreTest.registerAutoConfiguration(TransactionAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class);
    }

    @Override
    public Class<? extends Annotation>[] getPropertiesAnnotation() {
        return new Class[]{TransactionProperties.class};
    }
}

