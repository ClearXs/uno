package cc.allio.uno.component.http.openapi.v3;

import cc.allio.uno.component.http.openapi.Parser;
import cc.allio.uno.component.http.openapi.ParserContext;
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
public class SecuritySchemeInParser implements Parser<SecurityScheme.In> {
    @Override
    public void init(ParserContext context) {
        context.module().addDeserializer(SecurityScheme.In.class, new SecuritySchemeInDeserializer(context));
    }

    @Override
    public void preParse(ParserContext context) {

    }

    @Override
    public SecurityScheme.In parse(String unresolved, ParserContext context) {
        return Arrays.stream(SecurityScheme.In.values())
                .filter(style -> unresolved.equals(style.toString()))
                .findFirst()
                .orElseGet(() -> SecurityScheme.In.COOKIE);
    }

    static class SecuritySchemeInDeserializer extends StdDeserializer<SecurityScheme.In> {

        private final ParserContext parserContext;

        public SecuritySchemeInDeserializer(ParserContext parserContext) {
            this(null, parserContext);
        }

        protected SecuritySchemeInDeserializer(Class<?> vc, ParserContext parserContext) {
            super(vc);
            this.parserContext = parserContext;
        }


        @Override
        public SecurityScheme.In deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String text = p.getText();
            return parserContext
                    .execution()
                    .execute(SecuritySchemeInParser.class, text, parserContext);
        }
    }
}
