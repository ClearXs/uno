package cc.allio.uno.component.netty;

import cc.allio.uno.component.netty.processor.NettyProcessor;
import cc.allio.uno.component.netty.exception.RemoteSendException;
import cc.allio.uno.component.netty.exception.RemoteTimeoutException;
import cc.allio.uno.component.netty.filter.Filter;
import cc.allio.uno.component.netty.model.RemoteTransporter;
import cc.allio.uno.component.netty.transport.body.RequestBody;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ExecutorService;

/**
 * @author jiangw
 * @date 2020/12/7 21:02
 * @since 1.0
 */
public interface RemoteService {

    /**
     * 初始化远程 server/client
     */
    void init();

    /**
     * 启动服务.客户端或服务器端
     */
    void start();

    /**
     * 并不使用强制关闭
     *
     * @throws InterruptedException
     */
    void shutdownGracefully() throws InterruptedException;

    /**
     * 对请求时异步调用，对返回的结果时同步阻塞。
     *
     * @param channel 调用的通道
     * @param request 请求
     * @param timeout 超时时间
     */
    RemoteTransporter syncInvoke(Channel channel, RemoteTransporter request, long timeout) throws InterruptedException, RemoteTimeoutException, RemoteSendException;

    /**
     * 实现未阻塞的异步调用
     *
     * @param channel    {@link Channel}
     * @param request    {@link RemoteTransporter} {@link RequestBody}
     * @param returnType {@link Class} 期望返回值的类型
     * @return 一个promise对象，详细见{@link AbstractNettyService.AsyncPromise}
     */
    Object asyncInvoke(Channel channel, RemoteTransporter request, Class<?> returnType);

    /**
     * 关闭业务处理器器线程池
     */
    void shutdownProcessors();

    /**
     * 对于服务提供者，需要注册远程调用的处理器
     * 对于消费者，需要注册订阅结果的处理器。。。
     *
     * @param code      消息的类型{@link Protocol.Code}
     * @param processor 对于的处理器
     * @param exec      处理业务逻辑的线程池
     */
    void registerProcessors(byte code, NettyProcessor processor, ExecutorService exec);

    /**
     * 处理请求，由于这一部分需要耗费的时间比较久，所以针对每一类请求开启对应的线程池
     * 1.注册请求
     * 2.订阅请求
     * 3.远程调用请求。。
     *
     * @param ctx         {@link ChannelHandlerContext}
     * @param transporter {@link RemoteTransporter}
     */
    void processRemoteRequest(ChannelHandlerContext ctx, RemoteTransporter transporter);

    /**
     * 处理rpc的响应
     *
     * @param ctx         {@link ChannelHandlerContext}
     * @param transporter {@link RemoteTransporter}
     */
    void processRemoteResponse(ChannelHandlerContext ctx, RemoteTransporter transporter);

    /**
     * 添加入站过滤器
     *
     * @param filters {@link Filter}
     */
    void addInboundFilter(Filter... filters);

    /**
     * 添加出站过滤器
     *
     * @param filters {@link Filter}
     */
    void addOutboundFilter(Filter... filters);
}
