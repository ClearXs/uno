package cc.allio.uno.component.http.metadata.interceptor;

import cc.allio.uno.component.http.metadata.HttpRequestMetadata;
import cc.allio.uno.component.http.metadata.HttpResponseMetadata;
import cc.allio.uno.core.chain.Chain;
import cc.allio.uno.core.chain.ChainContext;
import com.google.auto.service.AutoService;
import reactor.core.publisher.Mono;

@AutoService(Interceptor.class)
public class CustomInterceptor implements Interceptor {
    @Override
    public Mono<HttpResponseMetadata> execute(Chain<HttpRequestMetadata, HttpResponseMetadata> chain, ChainContext<HttpRequestMetadata> context) throws Throwable {
        return null;
    }
}
