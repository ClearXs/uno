package cc.allio.uno.test.annotation.properties;

import cc.allio.uno.test.env.annotation.properties.*;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;
import java.time.Duration;
import java.util.Map;

public class DefaultAnnoPropertiesParserTest extends BaseTestCase {

    @Test
    void testNotAddPropertiesAnno() {
        AnnoPropertiesParser annoPropertiesParser = AnnoPropertiesParser.DEFAULT;
        UnknownProperties annotation = Demo.class.getAnnotation(UnknownProperties.class);
        Map<String, Object> properties = annoPropertiesParser.getProperties(annotation);
        assertEquals(0, properties.size());
    }

    @Test
    void testPropertiesAndPropertiesName() {
        AnnoPropertiesParser annoPropertiesParser = AnnoPropertiesParser.DEFAULT;
        SimpleProperties annotation = Demo.class.getAnnotation(SimpleProperties.class);
        Map<String, Object> properties = annoPropertiesParser.getProperties(annotation);
        assertEquals(2, properties.size());
        assertTrue(properties.containsKey("uno.value"));
        assertTrue(properties.containsKey("uno.test"));
    }

    @Test
    void testEmbeddedProperties() {
        AnnoPropertiesParser annoPropertiesParser = AnnoPropertiesParser.DEFAULT;
        EmbeddedProperties annotation = Demo.class.getAnnotation(EmbeddedProperties.class);
        Map<String, Object> properties = annoPropertiesParser.getProperties(annotation);
        assertEquals(3, properties.size());
        assertTrue(properties.containsKey("uno.value"));
        assertTrue(properties.containsKey("uno.sub.value"));
    }

    @Test
    void testAggr() {
        AnnoPropertiesParser annoPropertiesParser = AnnoPropertiesParser.DEFAULT;
        AggrProperties annotation = Demo.class.getAnnotation(AggrProperties.class);
        Map<String, Object> properties = annoPropertiesParser.getProperties(annotation);
        assertEquals(3, properties.size());
        assertEquals("12", properties.get("uno.sub.ex"));
    }

    @UnknownProperties
    @SimpleProperties
    @EmbeddedProperties(sub = @EmbeddedProperties.SubProperties)
    @AggrProperties(exclude = "12")
    @Sub
    public static class Demo {

    }

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface UnknownProperties {

    }

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Properties("uno")
    public @interface SimpleProperties {

        String value() default "";

        @PropertiesName("test")
        String testValue() default "";
    }

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Properties("uno")
    public @interface EmbeddedProperties {

        String value() default "";

        SubProperties sub();

        @Documented
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.TYPE)
        @interface SubProperties {

            String value() default "";

            @PropertiesType(Duration.class)
            long time() default 0;
        }
    }

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Properties("uno")
    @Sub
    public @interface AggrProperties {

        String value() default "";

        @AliasFor(annotation = Sub.class, attribute = "ex")
        String exclude() default "";

        @AliasFor(annotation = Sub.class, attribute = "in")
        String include() default "";
    }


    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Properties("uno.sub")
    public @interface Sub {

        String value() default "";

        @Empty
        String ex() default "";

        @Empty
        String in() default "";

    }
}
