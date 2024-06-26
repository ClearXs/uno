package cc.allio.uno.core.util.template.mvel;

import cc.allio.uno.core.BaseTestCase;
import cc.allio.uno.core.util.DateUtil;
import cc.allio.uno.core.util.template.ExpressionTemplate;
import cc.allio.uno.core.util.template.TemplateContext;
import lombok.Data;
import org.junit.jupiter.api.Test;

public class MVELExpressionTemplateTest extends BaseTestCase {

    @Test
    void testOrbTemplate() {
        String template = "Hello, my name is @{person.name} email @{person.email.name}";

        MVELExpressionTemplate mvelExpressionTemplate = ExpressionTemplate.createMVEL();
        TemplateContext templateContext = new TemplateContext();
        Person person = new Person();
        Email email = new Email();
        person.setName("1");
        email.setName("2");
        person.setEmail(email);
        templateContext.putAttribute("person", person);
        String resolved = mvelExpressionTemplate.parseTemplate(template, templateContext);

        assertEquals("Hello, my name is 1 email 2", resolved);
    }

    @Test
    void testUtilityMethod() {
        String template = "time is @{date.formatNow()}";
        MVELExpressionTemplate mvelExpressionTemplate = ExpressionTemplate.createMVEL();
        TemplateContext templateContext = new TemplateContext();
        String resolved = mvelExpressionTemplate.parseTemplate(template, templateContext);
        System.out.println(resolved);
    }

    @Data
    public static class Person {

        public String name;

        public Email email;
    }


    @Data
    public static class Email {
        public String name;
    }
}
