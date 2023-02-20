package cc.allio.uno.component.sequential.convert;

import cc.allio.uno.component.sequential.Sequential;
import cc.allio.uno.core.bean.ObjectWrapper;
import cc.allio.uno.core.metadata.convert.AbstractJsonConverter;
import reactor.core.publisher.Mono;

public class TestSequentialConvert extends AbstractJsonConverter<Sequential> {

    public TestSequentialConvert(Class<? extends Sequential> convertType) {
        super(convertType);
    }

    @Override
    protected Mono<Void> executeAssignmentDefaultAction(Sequential sequential, ObjectWrapper wrapper) {
        return Mono.empty();
    }
}
