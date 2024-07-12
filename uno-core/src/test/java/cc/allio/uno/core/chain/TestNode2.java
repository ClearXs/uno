package cc.allio.uno.core.chain;

import reactor.core.publisher.Mono;

import javax.annotation.Priority;

@Priority(2)
public class TestNode2 implements Node<String, String> {

    @Override
    public Mono<String> execute(Chain<String, String> chain, ChainContext<String> context) throws Throwable {
        context.getAttribute().put("TestNode2", "TestNode2");
        return chain.proceed(context);
    }
}
