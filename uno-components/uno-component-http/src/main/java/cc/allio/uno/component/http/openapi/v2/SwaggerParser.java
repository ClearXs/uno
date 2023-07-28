package cc.allio.uno.component.http.openapi.v2;

import cc.allio.uno.core.serializer.JacksonSerializer;
import cc.allio.uno.core.serializer.Serializer;
import cc.allio.uno.core.serializer.SerializerHolder;
import cc.allio.uno.component.http.openapi.Parser;
import cc.allio.uno.component.http.openapi.ParserContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.Parameter;
import java.io.IOException;

public class SwaggerParser implements Parser<Swagger> {


    @Override
    public void init(ParserContext context) {

    }

    @Override
    public void preParse(ParserContext context) {

    }

    @Override
    public Swagger parse(String unresolved, ParserContext context) {
        Serializer serializer = SerializerHolder.holder().get();
        ((JacksonSerializer) serializer).registerWorker(Swagger.class.getName(), worker -> {
            SimpleModule module = new SimpleModule();
            module.addDeserializer(io.swagger.models.parameters.Parameter.class, new ParameterDeserializer());
            worker.registerModule(module);
        });
        return SerializerHolder.holder().get()
                .deserialize(unresolved.getBytes(), Swagger.class);
    }


    static class ParameterDeserializer extends StdDeserializer<Parameter> {

        public ParameterDeserializer() {
            this(null);
        }

        protected ParameterDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public io.swagger.models.parameters.Parameter deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }
    }
}
