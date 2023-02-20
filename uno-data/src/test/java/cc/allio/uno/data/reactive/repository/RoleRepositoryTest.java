package cc.allio.uno.data.reactive.repository;

import cc.allio.uno.data.model.Role;
import cc.allio.uno.data.reactive.R2dbcConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.ContextConfiguration;
import reactor.test.StepVerifier;

@DataR2dbcTest
@ContextConfiguration(classes = R2dbcConfiguration.class)
public class RoleRepositoryTest {

    @Autowired
    private  RoleReactiveRepository roleReactiveRepository;

    @Test
    void testSaveOne() {
        Role role = new Role();
        role.setId(1L);
        role.setName("role");

        roleReactiveRepository.save(role)
                .thenMany(roleReactiveRepository.findByNameContains("role"))
                .count()
                .as(StepVerifier::create)
                .expectNext(1L)
                .verifyComplete();

        roleReactiveRepository.delete(role)
                .as(StepVerifier::create)
                .verifyComplete();

    }
}
