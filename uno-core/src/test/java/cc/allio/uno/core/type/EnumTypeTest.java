package cc.allio.uno.core.type;

import cc.allio.uno.core.BaseTestCase;
import org.junit.jupiter.api.Test;

public class EnumTypeTest extends BaseTestCase {

    @Test
    void testEnumOperator() {
        TypeOperator translator = TypeOperatorFactory.translator(TestA.class);
        Object convert = translator.convert("A", TestA.class);
        System.out.println(convert);
    }

    public class User {
        private TestA testA;
    }

    public enum TestA {
        A,
        B
    }
}
