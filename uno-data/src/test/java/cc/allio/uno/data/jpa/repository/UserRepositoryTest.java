package cc.allio.uno.data.jpa.repository;

import cc.allio.uno.data.jpa.JpaConfiguration;
import cc.allio.uno.data.model.Role;
import cc.allio.uno.data.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

@DataJpaTest
@ContextConfiguration(classes = JpaConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest extends Assertions {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Test
    void testSaveOne() {
        User user = new User();
        user.setName("name");
        userRepository.save(user);

        List<User> users = userRepository.findByName("name");
        assertEquals(1, users.size());
        User user0 = users.get(0);
        assertEquals(user.getName(), user0.getName());
    }

    /**
     * Test Case: 测试级联保存
     */
    @Test
    void testRoleManyToManyCascadeSave() {
        save();
        List<User> users = userRepository.findByName("name");
        assertEquals(1, users.size());
        User user0 = users.get(0);
        assertEquals("name", user0.getName());
//        assertEquals(1, user0.getRoles().size());
    }


    /**
     * Test Case: 测试多对多级联删除
     */
    @Test
    void testRoleManyToManyCascadeDelete() {
        save();

        int deleteUserCount = userRepository.deleteByName("name");
        assertEquals(1, deleteUserCount);

        List<Role> roles = roleRepository.findByName("name");
        assertEquals(0, roles.size());
    }

    void save() {
        User user = new User();
        user.setName("name");
        Role role = new Role();
        role.setName("name");
        // 需要在数据库中存在该实例
        role = roleRepository.save(role);
//        user.setRoles(Sets.newHashSet(role));
        userRepository.save(user);
    }
}
