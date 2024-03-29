package cc.allio.uno.core.bean;

import cc.allio.uno.core.User;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.Map;

public class ObjectWrapperTest {

    @Test
    void testGet() {
        User user = new User();
        user.setName("name");
        ObjectWrapper wrapper = new ObjectWrapper(user);
        wrapper.get("name")
                .as(StepVerifier::create)
                .expectNext("name")
                .verifyComplete();
    }

    @Test
    void testSet() {
        User user = new User();
        ObjectWrapper wrapper = new ObjectWrapper(user);
        wrapper.set("name", "name")
                .map(o -> ((User) o).getName())
                .as(StepVerifier::create)
                .expectNext("name")
                .verifyComplete();
    }

    @Test
    void testSetCoverage() {
        User user = new User();
        user.setName("name");
        ObjectWrapper wrapper = new ObjectWrapper(user);
        wrapper.setCoverage("name", false, "name1")
                .map(o -> ((User) o).getName())
                .as(StepVerifier::create)
                .expectNext("name")
                .verifyComplete();
        wrapper.setCoverage("name", true, "name1")
                .map(o -> ((User) o).getName())
                .as(StepVerifier::create)
                .expectNext("name1")
                .verifyComplete();
    }

    @Test
    void testGetAllValues() {
        User user = new User();
        user.setName("name");
        user.setType("type");
        ObjectWrapper wrapper = new ObjectWrapper(user);
        Map<String, Object> allValuesForce = wrapper.findMapValuesForce();
        System.out.println(allValuesForce);

        wrapper.get("id")
                .as(StepVerifier::create)
                .expectNext(ValueWrapper.EMPTY_VALUE)
                .verifyComplete();

    }
}
