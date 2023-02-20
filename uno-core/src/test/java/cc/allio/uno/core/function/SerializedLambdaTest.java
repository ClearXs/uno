package cc.allio.uno.core.function;

import cc.allio.uno.core.BaseTestCase;
import cc.allio.uno.core.User;
import org.junit.jupiter.api.Test;

class SerializedLambdaTest extends BaseTestCase {

    @Test
    void testGetStaticMethodReference() {
        StaticMethodReference<User> reference = User::getName;
        SerializedLambda lambda = SerializedLambda.of(reference);
        assertEquals("name", lambda.getFieldName());
    }

    @Test
    void testMethodReference() {
        User user = new User();
        MethodReference<String> reference = user::getName;
        SerializedLambda lambda = SerializedLambda.of(reference);
        assertEquals("name", lambda.getFieldName());
    }
}
