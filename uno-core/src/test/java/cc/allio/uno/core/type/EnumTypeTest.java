package cc.allio.uno.core.type;

import cc.allio.uno.core.BaseTestCase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;

public class EnumTypeTest extends BaseTestCase {

    @Test
    void testEnumOperator() {
        TypeOperator translator = TypeOperatorFactory.translator(TestA.class);
        Object convert = translator.convert("A", TestA.class);
        System.out.println(convert);
    }

    @Test
    void testGuessEnum() {
        TypeOperator<Named> translator1 = TypeOperatorFactory.translator(Named.class);

        Named name = translator1.convert("name");

        assertNotNull(name);

        TypeOperator<NameValued> translator2 = TypeOperatorFactory.translator(NameValued.class);

        NameValued value = translator2.convert("value");
        assertNotNull(value);

        NameValued label = translator2.convert("label");
        assertNotNull(label);

        NameValued value1 = ((EnumTypeOperator<NameValued>) translator2).convert("value", NameValued::getValue);

        assertNotNull(value);
    }


    public class User {
        private TestA testA;
    }

    public enum TestA {
        A,
        B
    }

    @Getter
    @AllArgsConstructor
    public enum Named {

        NAME("name");

        private final String label;
    }

    @Getter
    @AllArgsConstructor
    public enum NameValued {

        NAME("value", "label");
        private final String value;
        private final String label;
    }
}
