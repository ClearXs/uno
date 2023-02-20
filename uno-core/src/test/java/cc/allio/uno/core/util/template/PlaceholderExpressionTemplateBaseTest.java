package cc.allio.uno.core.util.template;

import cc.allio.uno.core.BaseTestCase;
import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import com.ql.util.express.IExpressContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.annotation.processing.FilerException;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
class PlaceholderExpressionTemplateBaseTest extends BaseTestCase {

    private ExpressionTemplate template;

    private User user;

    @Override
    protected void onInit() throws Throwable {
        template = new PlaceholderExpressionTemplate(Tokenizer.HASH_BRACE);
        user = new User();
        user.setId("1");
        System system = new System();
        system.setId("2");
        user.setSystem(system);
    }

    @Test
    void testFileSuffixError() {
        assertThrows(FilerException.class, () -> {
            template.parseFileTemplate("test.txt", user);
        });
    }

    @Test
    void testFileNotFound() {
        assertThrows(FileNotFoundException.class, () -> {
            template.parseFileTemplate("test.template", user);
        });
    }

    @Test
    void testTemplateParser() {
        assertDoesNotThrow(() -> {
            String template = this.template.parseFileTemplate("cc/allio/uno/core/util/template/example.template", user);
            log.info(template);
        });
    }

    @Test
    void testTemplateMap() {
        assertDoesNotThrow(() -> {
            Map<String, Object> map = new HashMap<>();
            map.put("test", 1);
            User user = new User();
            user.setId("2");
            map.put("user", user);
            user.test.put("m2", "m2");
            String template = this.template.parseFileTemplate("cc/allio/uno/core/util/template/exampleMap.template", map);
            log.info(template);
        });
    }

    @Test
    void testQLExpress() {
        assertDoesNotThrow(() -> {
            String template = this.template.parseFileTemplate("cc/allio/uno/core/util/template/example.template", user);
            ExpressRunner runner = new ExpressRunner(false, false);
            IExpressContext<String, Object> expressContext = new DefaultContext<>();
            expressContext.put("user", user);
            runner.execute(template, expressContext, Collections.emptyList(), false, false);
            log.info("user: {}", user);
        });
    }

    @Override
    protected void onDown() throws Throwable {

    }

    @Data
    public static class User {
        private String id;
        private System system;
        private String result;
        private Map<String, Object> test = new HashMap<>();
    }

    @Data
    public static class System {
        private String id;
    }
}
