package cc.allio.uno.websocket.demo;

import cc.allio.uno.websocket.BaseWebsocketEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.FluxSink;

import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.List;

@Slf4j
@Controller
@ServerEndpoint("/demo")
public class DemoWebSocketEndpoint extends BaseWebsocketEndpoint<String> {

    @Override
    protected void doOnOpen(Session session) throws Throwable {

    }

    @Override
    protected void doOnMessage(Session session, List<String> message) throws Throwable {
        for (String s : message) {
            log.info(s);
        }
    }

    @Override
    protected void doOnPublish(Object source, FluxSink<String> collectSink, Session session) throws Throwable {

    }

    @Override
    protected void doOnClose() throws Throwable {

    }

    @Override
    protected void doOnError() throws Throwable {

    }

    @Override
    public boolean enableHeartbeat() {
        return false;
    }
}
