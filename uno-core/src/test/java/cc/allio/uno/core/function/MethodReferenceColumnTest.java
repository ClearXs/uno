package cc.allio.uno.core.function;

import cc.allio.uno.core.BaseTestCase;
import cc.allio.uno.core.function.lambda.MethodReferenceColumn;
import lombok.Data;
import org.junit.jupiter.api.Test;

public class MethodReferenceColumnTest extends BaseTestCase {

    @Test
    void testGetEntityType() {
        MethodReferenceColumn<User> methodReferenceColumn = User::getId;
        assertEquals(User.class, methodReferenceColumn.getEntityType());
    }

    @Data
    public static class User {
        private String id;
    }
}
