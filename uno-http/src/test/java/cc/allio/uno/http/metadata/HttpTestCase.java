package cc.allio.uno.http.metadata;

import cc.allio.uno.test.BaseTestCase;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.function.Consumer;

/**
 * Http测试
 *
 * @author j.x
 * @since 1.0
 */
public abstract class HttpTestCase extends BaseTestCase {

    MockWebServer mockWebServer;

    @Override
    protected void onInit() throws Throwable {
        mockWebServer = new MockWebServer();
    }

    protected void prepareResponse(Consumer<MockResponse> consumer) {
        MockResponse response = new MockResponse();
        consumer.accept(response);
        mockWebServer.enqueue(response);
    }

    protected WebClient buildWebClient() {
        return WebClient
                .builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();
    }

    @Override
    protected void onDown() throws Throwable {
        mockWebServer.shutdown();
    }
}
