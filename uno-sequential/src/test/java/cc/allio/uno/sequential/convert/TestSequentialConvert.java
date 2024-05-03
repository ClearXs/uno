package cc.allio.uno.sequential.convert;

import cc.allio.uno.core.bean.BeanWrapper;
import cc.allio.uno.core.metadata.convert.AbstractJsonConverter;
import cc.allio.uno.sequnetial.Sequential;
import reactor.core.publisher.Mono;

public class TestSequentialConvert extends AbstractJsonConverter<Sequential> {

    public TestSequentialConvert(Class<? extends Sequential> convertType) {
        super(convertType);
    }

    @Override
    protected Mono<Void> executeAssignmentDefaultAction(Sequential sequential, BeanWrapper wrapper) {
        return Mono.empty();
    }
}
