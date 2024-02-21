package cc.allio.uno.core.chain;

import reactor.core.publisher.Mono;

public class ErrorNode implements Node<String, String> {
    @Override
    public Mono<String> execute(Chain<String, String> chain, ChainContext<String> context) throws Throwable {
        int i = 1 / 0;
        return chain.proceed(context);
    }
}
