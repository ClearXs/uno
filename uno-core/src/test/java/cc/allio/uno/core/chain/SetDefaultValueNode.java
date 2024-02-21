package cc.allio.uno.core.chain;

import reactor.core.publisher.Mono;

public class SetDefaultValueNode implements Node<String, String> {

    @Override
    public Mono<String> execute(Chain<String, String> chain, ChainContext<String> context) throws Throwable {
        context.getAttribute().put("test", "default");
        return chain.proceed(context);
    }
}
