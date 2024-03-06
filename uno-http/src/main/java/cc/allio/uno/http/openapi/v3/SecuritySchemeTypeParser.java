package cc.allio.uno.http.openapi.v3;

import cc.allio.uno.http.openapi.Parser;
import cc.allio.uno.http.openapi.ParserContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.io.IOException;
import java.util.Arrays;

/**
 * Todo
 *
 * @author jw
 * @date 2021/12/6 14:05
 */
public class SecuritySchemeTypeParser implements Parser<SecurityScheme.Type> {
    @Override
    public void init(ParserContext context) {
        context.module().addDeserializer(SecurityScheme.Type.class, new SecuritySchemeTypeDeserializer(context));
    }

    @Override
    public void preParse(ParserContext context) {
        // TODO
    }

    @Override
    public SecurityScheme.Type parse(String unresolved, ParserContext context) {
        return Arrays.stream(SecurityScheme.Type.values())
                .filter(style -> unresolved.equals(style.toString()))
                .findFirst()
                .orElseGet(() -> SecurityScheme.Type.APIKEY);
    }

    static class SecuritySchemeTypeDeserializer extends StdDeserializer<SecurityScheme.Type> {

        private final ParserContext parserContext;

        public SecuritySchemeTypeDeserializer(ParserContext parserContext) {
            this(null, parserContext);
        }

        protected SecuritySchemeTypeDeserializer(Class<?> vc, ParserContext parserContext) {
            super(vc);
            this.parserContext = parserContext;
        }

        @Override
        public SecurityScheme.Type deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String text = p.getText();
            return parserContext
                    .execution()
                    .execute(SecuritySchemeTypeParser.class, text, parserContext);
        }
    }
}
