package cc.allio.uno.core.metadata.source;

import cc.allio.uno.core.bean.ObjectWrapper;
import cc.allio.uno.core.metadata.UserMetadata;
import cc.allio.uno.core.metadata.convert.AbstractJsonConverter;
import cc.allio.uno.core.metadata.convert.Converter;
import cc.allio.uno.core.metadata.endpoint.source.SourceConverter;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.context.ApplicationContext;
import reactor.core.publisher.Mono;

public class TestSourceConverter implements SourceConverter<UserMetadata> {

    @Override
    public JsonNode prepare(ApplicationContext context, JsonNode value) throws Throwable {
        return value;
    }

    @Override
    public UserMetadata doConvert(ApplicationContext context, JsonNode value) throws Throwable {
        Converter<UserMetadata> converter = new AbstractJsonConverter<UserMetadata>(UserMetadata.class) {

            @Override
            protected Mono<Void> executeAssignmentDefaultAction(UserMetadata metadata, ObjectWrapper wrapper) {
                return Mono.defer(() -> {
                    metadata.setId("id");
                    return Mono.empty();
                });
            }
        };
        return converter.execute(context, value);
    }

}
