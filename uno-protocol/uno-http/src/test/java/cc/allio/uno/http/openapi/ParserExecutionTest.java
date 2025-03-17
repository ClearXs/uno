package cc.allio.uno.http.openapi;

import cc.allio.uno.core.serializer.SerializerHolder;
import cc.allio.uno.test.BaseTestCase;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;

import lombok.Data;
import org.junit.jupiter.api.Test;

/**
 * 解析器执行者单元测试用例
 *
 * @author j.x
 */
class ParserExecutionTest extends BaseTestCase {

    private static DefaultParserContext context;

    String userJson = "{\"id\":1,\"item\":{\"id\":2,\"itemName\":\"theItem\"}}";

    @Override
    protected void onInit() throws Throwable {
        ParserExecution execution = new ParserExecution();
        ObjectMapper mapper = new ObjectMapper();
        context = new DefaultParserContext(mapper, execution);
        context.setSerializer(SerializerHolder.holder().get());
    }

    /**
     * 测试执行者注册Parser
     */
    @Test
    void testRegister() {
        // 1. 测试注册
        UserParser userParser = new UserParser();
        assertDoesNotThrow(() -> context.execution().register(userParser, context));
        ParserExecution execution = context.execution();
        // 2.测试抛出IllegalArgumentException异常
        assertThrows(IllegalArgumentException.class, () -> execution.register(null, context));
        // 3.测试抛出NullPointerException
        assertThrows(NullPointerException.class, () -> execution.register(null, userParser, context));
    }

    /**
     * 测试当执行者在缓存中没有找到解析器时抛出异常
     */
    @Test
    void testExecuteProcessParserIsNull() {
        UserParser userParser = new UserParser();
        ParserExecution execution = context.execution();
        Class<? extends UserParser> parserClass = userParser.getClass();
        assertThrows(NullPointerException.class, () -> execution.execute(parserClass, userJson, context));
    }

    /**
     * 测试执行者执行方法
     */
    @Test
    void testExecute() {
        UserParser userParser = new UserParser();
        ParserExecution execution = context.execution();
        execution.register(userParser, context);
        User user = execution.execute(UserParser.class, userJson, context);
        assertEquals(1, user.getId());
        assertEquals(2, user.getItem().getId());
        assertEquals("theItem", user.getItem().getItemName());

        // json串为空时
        User execute = execution.execute(UserParser.class, "", context);
        assertNull(execute);
    }


    @Data
    static class Item {
        private long id;

        private String itemName;
    }

    @Data
    static class User {

        private long id;

        private Item item;
    }

    static class UserParser implements Parser<User> {

        @Override
        public void init(ParserContext context) {
            SimpleModule module = context.module();
            if (module == null) {
                module = new SimpleModule();
                ((DefaultParserContext) context).setModule(module);
            }
            context.module().addDeserializer(Item.class, new StdDeserializer<Item>((Class<?>) null) {
                @Override
                public Item deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                    return null;
                }
            });
            context.mapper().registerModule(module);
        }

        @Override
        public void preParse(ParserContext context) {

        }

        @Override
        public User parse(String unresolved, ParserContext context) {
            return context.serializer().deserialize(unresolved.getBytes(), User.class);
        }

    }
}
