package cc.allio.uno.data.reactive.repository;

import cc.allio.uno.data.model.Role;
import cc.allio.uno.data.model.User;
import cc.allio.uno.data.reactive.R2dbcConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.ContextConfiguration;
import reactor.test.StepVerifier;

@DataR2dbcTest
@ContextConfiguration(classes = R2dbcConfiguration.class)
class UserReactiveRepositoryTest {

    @Autowired
    private UserReactiveRepository userRepository;
    @Autowired
    private RoleReactiveRepository roleRepository;


    @Test
    void testSaveOne() {
        User user = new User();
        user.setId(1L);
        user.setName("name");
        userRepository.save(user)
                .thenMany(userRepository.findByNameContains("name"))
                .count()
                .as(StepVerifier::create)
                .expectNext(1L)
                .verifyComplete();

        userRepository.delete(user)
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void testCascadeRole() {
        User user = new User();
        user.setId(1L);
        user.setName("name");
        Role role = new Role();
        role.setId(1L);
        role.setName("role");
        user.setRoleId(role.getId());
        roleRepository.save(role)
                .then(userRepository.save(user))
                .thenMany(userRepository.findByNameContains("name"))
                .flatMap(thenUser -> roleRepository.findById(user.getRoleId()))
                .count()
                .as(StepVerifier::create)
                .verifyComplete();
    }
}
