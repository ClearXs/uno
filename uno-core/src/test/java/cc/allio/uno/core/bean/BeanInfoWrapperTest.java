package cc.allio.uno.core.bean;

import cc.allio.uno.core.BaseTestCase;
import cc.allio.uno.core.User;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Data;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.beans.IntrospectionException;
import java.util.List;
import java.util.Set;

/**
 * BeanInfoWrapper 单元测试
 *
 * @author jiangwei
 * @date 2022/7/3 00:12
 * @since 1.0
 */
class BeanInfoWrapperTest extends BaseTestCase {

    @Test
    void testGetUserNull() throws IntrospectionException {
        User user = new User();
        user.setName("name");
        BeanInfoWrapper<User> infoWrapper = new BeanInfoWrapper<>(User.class);
        infoWrapper.get(user, "name", String.class)
                .as(StepVerifier::create)
                .expectNext("name")
                .verifyComplete();
    }

    @Test
    void testSet() throws IntrospectionException {
        User user = new User();
        BeanInfoWrapper<User> infoWrapper = new BeanInfoWrapper<>(User.class);
        infoWrapper.set(user, "name", "name")
                .map(User::getName)
                .as(StepVerifier::create)
                .expectNext("name")
                .verifyComplete();
    }

    @Test
    void testForceSet() throws IntrospectionException {
        User user = new User();
        user.setName("name");
        BeanInfoWrapper<User> infoWrapper = new BeanInfoWrapper<>(User.class);
        infoWrapper.setCoverage(user, "name", true, "name1")
                .map(User::getName)
                .as(StepVerifier::create)
                .expectNext("name1")
                .verifyComplete();
    }

    @Test
    void testNull() throws IntrospectionException {
        User user = new User();
        BeanInfoWrapper<User> infoWrapper = new BeanInfoWrapper<>(User.class);
        infoWrapper.set(user, "name", null).subscribe();
    }

    @Test
    void testSetArray() throws IntrospectionException {
        TestComplex testComplex = new TestComplex();
        BeanInfoWrapper<TestComplex> infoWrapper = new BeanInfoWrapper<>(TestComplex.class);
        infoWrapper.set(testComplex, "arrays", Lists.newArrayList("2", "2")).subscribe();

        assertEquals(2, testComplex.getArrays().length);
    }

    @Test
    void testSetList() throws IntrospectionException {
        TestComplex testComplex = new TestComplex();
        BeanInfoWrapper<TestComplex> infoWrapper = new BeanInfoWrapper<>(TestComplex.class);
        infoWrapper.set(testComplex, "list", Lists.newArrayList("2", "2")).subscribe();

        assertEquals(2, testComplex.getList().size());
    }

    @Test
    void testSetSet() throws IntrospectionException {
        TestComplex testComplex = new TestComplex();
        BeanInfoWrapper<TestComplex> infoWrapper = new BeanInfoWrapper<>(TestComplex.class);
        infoWrapper.set(testComplex, "set", Sets.newHashSet("2")).subscribe();

        assertEquals(1, testComplex.getSet().size());
    }

    @Data
    public class TestComplex {
        private String[] arrays;

        private List<String> list;

        private Set<String> set;
    }
}
