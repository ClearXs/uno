package cc.allio.uno.core.bean;

import cc.allio.uno.core.BaseTestCase;
import cc.allio.uno.core.User;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.beans.IntrospectionException;

/**
 * BeanInfoWrapper 单元测试
 *
 * @author jiangwei
 * @date 2022/7/3 00:12
 * @since 1.0
 */
class BeanInfoWrapperTest extends BaseTestCase {

    @Override
    protected void onInit() throws Throwable {

    }

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

    @Override
    protected void onDown() throws Throwable {

    }

}
