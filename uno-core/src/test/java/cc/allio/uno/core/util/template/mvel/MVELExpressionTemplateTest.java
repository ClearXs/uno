package cc.allio.uno.core.util.template.mvel;

import cc.allio.uno.core.BaseTestCase;
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
    void testMapParams() {
        String template = "Hello, @{name}";
        MVELExpressionTemplate mvelExpressionTemplate = ExpressionTemplate.createMVEL();
        TemplateContext templateContext = new TemplateContext();
        templateContext.putAttribute("name", "测试");
        String out = mvelExpressionTemplate.parseTemplate(template, templateContext);
        assertEquals("Hello, 测试", out);
    }

    @Test
    void testUtilityMethod() {
        String template1 = "time is @{date.formatNow()}";
        MVELExpressionTemplate mvelExpressionTemplate = ExpressionTemplate.createMVEL();
        TemplateContext templateContext = new TemplateContext();
        String resolved = mvelExpressionTemplate.parseTemplate(template1, templateContext);
        System.out.println(resolved);

        String template2 = "@{string.camelToUnderline(AB)}";
        Person person = new Person();
        person.setName("ClickOver");
        templateContext.addImport(Person.class);
        templateContext.putAttribute("person", person);
        String r2 = mvelExpressionTemplate.parseTemplate(template2, templateContext);
        System.out.println(r2);
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
