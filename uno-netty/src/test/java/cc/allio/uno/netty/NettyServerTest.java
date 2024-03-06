package cc.allio.uno.netty;

import cc.allio.uno.netty.concurrent.Callback;
import cc.allio.uno.netty.exception.ConnectionException;
import cc.allio.uno.netty.transport.RemoteAddress;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

class NettyServerTest extends BaseTestCase {

    NettyServer nettyServer;

    void startup() {
        nettyServer = new NettyServer();
        nettyServer.start();
    }

    @Test
    void testSendMessage() throws ExecutionException, InterruptedException {
        CompletableFuture.runAsync(this::startup);
        NettyClient client = new NettyClient(new RemoteAddress("localhost", 8080));
        try {
            client.connect(new NettyChannelGroup(), new Callback() {
                @Override
                public void acceptable(Object accept) throws ConnectionException {
                    System.out.println(accept);
                }

                @Override
                public void rejected(Throwable ex) {
                    System.out.println(ex);
                }
            });

        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }
}
