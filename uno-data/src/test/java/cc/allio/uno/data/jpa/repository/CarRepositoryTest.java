package cc.allio.uno.data.jpa.repository;

import cc.allio.uno.data.model.Car;
import cc.allio.uno.data.model.User;
import cc.allio.uno.data.jpa.JpaConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;

@DataJpaTest
@ContextConfiguration(classes = JpaConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CarRepositoryTest extends Assertions {

    /**
     * 默认事物定义，事物传播行为与隔离级别
     */
    private static final DefaultTransactionDefinition DEFINITION = new DefaultTransactionDefinition();

    static {
        DEFINITION.setReadOnly(false);
        DEFINITION.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    @Autowired
    private CarRepository carRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PlatformTransactionManager transactionManager;

    @Test
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    void testSaveOne() {
        TransactionStatus status = transactionManager.getTransaction(DEFINITION);
        User user = new User();
        user.setName("name");
        user = userRepository.save(user);
        Car car = new Car();
        car.setBrand("brand");
        car.setUserId(user.getId());
        carRepository.save(car);
        transactionManager.commit(status);
        List<Car> cars = carRepository.findByUserId(user.getId());
        assertEquals(1, cars.size());
    }
}
