package cc.allio.uno.core.chain;

import jakarta.annotation.Priority;
import reactor.core.publisher.Mono;

@Priority(1)
public class TestNode1 implements Node<String, String> {

    @Override
    public Mono<String> execute(Chain<String, String> chain, ChainContext<String> context) throws Throwable {
        context.getAttribute().put("TestNode1", "TestNode1");
        return chain.proceed(context);
    }

}
