package cc.allio.uno.core.chain;

import com.google.common.collect.Lists;
import reactor.core.publisher.Flux;

public class ManyNode implements Node<Integer, Integer> {

    @Override
    public Flux<Integer> executeMany(Chain<Integer, Integer> chain, ChainContext<Integer> context) throws Throwable {
        return Flux.fromIterable(Lists.newArrayList(1, 2, 3))
                .map(v -> context.getIN() + v);
    }
}
