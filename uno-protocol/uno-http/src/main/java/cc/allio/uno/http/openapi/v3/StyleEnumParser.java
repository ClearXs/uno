package cc.allio.uno.http.openapi.v3;

import cc.allio.uno.http.openapi.Parser;
import cc.allio.uno.http.openapi.ParserContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import io.swagger.v3.oas.models.parameters.Parameter;
import java.io.IOException;
import java.util.Arrays;

/**
 * Todo
 * @author j.x
 */
public class StyleEnumParser implements Parser<Parameter.StyleEnum> {

    @Override
    public void init(ParserContext context) {
        context.module().addDeserializer(Parameter.StyleEnum.class, new StyleEnumDeserializer(context));
    }

    @Override
    public void preParse(ParserContext context) {
        // TODO
    }

    @Override
    public Parameter.StyleEnum parse(String unresolved, ParserContext context) {
        return Arrays.stream(Parameter.StyleEnum.values())
                .filter(style -> unresolved.equals(style.toString()))
                .findFirst()
                .orElseGet(() -> Parameter.StyleEnum.FORM);
    }


    static class StyleEnumDeserializer extends StdDeserializer<Parameter.StyleEnum> {

        private final ParserContext parserContext;

        public StyleEnumDeserializer(ParserContext parserContext) {
            this(null, parserContext);
        }

        protected StyleEnumDeserializer(Class<?> vc, ParserContext parserContext) {
            super(vc);
            this.parserContext = parserContext;
        }

        @Override
        public Parameter.StyleEnum deserialize(JsonParser p, DeserializationContext context) throws IOException {
            String text = p.getText();
            return parserContext
                    .execution()
                    .execute(StyleEnumParser.class, text, parserContext);
        }
    }
}
