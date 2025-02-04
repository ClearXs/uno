package cc.allio.uno.websocket;

import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import reactor.core.publisher.FluxSink;

import java.util.List;

@ServerEndpoint("/ws")
public class DemoWsEndpoint extends BaseWsEndpoint<String> {

    @Override
    protected void doOnOpen(Session session) throws Throwable {

    }

    @Override
    protected void doOnMessage(Session session, List<String> message) throws Throwable {

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
        return true;
    }
}
