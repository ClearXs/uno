package cc.allio.uno.netty.processor;

import cc.allio.uno.netty.AbstractNettyService;
import cc.allio.uno.netty.Protocol;
import cc.allio.uno.netty.model.RemoteTransporter;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ExecutorService;

/**
 * 针对某一类类型创建其处理器，远程调用请求，解析请求，调用服务，注册请求，订阅定期...
 * <p>
 *     一个Processor对应一个业务线程池，由{@link AbstractNettyService#processRemoteRequest(ChannelHandlerContext, RemoteTransporter)}
 *     进行调用{{@link #process(ChannelHandlerContext, RemoteTransporter)}}
 * </p>
 * <p>
 *     使用{@link AbstractNettyService#registerProcessors(byte, NettyProcessor, ExecutorService)}对某一类{@link NettyProcessor}进行注册
 * </p>
 * @see Protocol
 * @author jiangw
 * @date 2020/11/26 17:08
 * @since 1.0
 */
public interface NettyProcessor {

    /**
     * 根据某一类{@link Protocol.Code}处理对应的业务请求
     * @param ctx {@link ChannelHandlerContext}
     * @param request {@link RemoteTransporter}
     * @return 处理的结果
     */
    RemoteTransporter process(ChannelHandlerContext ctx, RemoteTransporter request);
}
