package cc.allio.uno.data.jpa.repository;

import cc.allio.uno.data.jpa.JpaConfiguration;
import cc.allio.uno.data.orm.jpa.UnoJpaAutoConfiguration;
import cc.allio.uno.data.model.Dept;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;

@DataJpaTest
@ContextConfiguration(classes = {JpaConfiguration.class, UnoJpaAutoConfiguration.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DeptRepositoryTest {

    @Autowired
    private DeptRepository repository;
    @Autowired
    private PlatformTransactionManager transactionManager;
    /**
     * 默认事物定义，事物传播行为与隔离级别
     */
    private static final DefaultTransactionDefinition DEFINITION = new DefaultTransactionDefinition();

    static {
        DEFINITION.setReadOnly(false);
        DEFINITION.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }


    @Test
    void testInsert() {
        TransactionStatus status = transactionManager.getTransaction(DEFINITION);
        Dept dept = new Dept();
        dept.setUserIds(Lists.newArrayList("1", "2"));
        repository.save(dept);
        transactionManager.commit(status);
    }

    @Test
    void testSelect() {
        List<Dept> depts = repository.findAll();
        System.out.println(depts);
    }
}
