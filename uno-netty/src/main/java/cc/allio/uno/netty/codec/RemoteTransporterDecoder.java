package cc.allio.uno.netty.codec;

import cc.allio.uno.netty.Protocol;
import cc.allio.uno.netty.exception.RemoteException;
import cc.allio.uno.netty.model.RemoteTransporter;
import cc.allio.uno.core.serializer.SerializerHolder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class RemoteTransporterDecoder extends ReplayingDecoder<Protocol.State> {

    private static final int MAX_BODY_LENGTH = 20 * 1024 * 1024;

    private final Protocol header;

    public RemoteTransporterDecoder() {
        // 初始化时，记录bytebuf状态为模式
        super(Protocol.State.HEADER_MAGIC);
        header = new Protocol();
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        switch (state()) {
            case HEADER_MAGIC:
                validateMagic(in.readInt());
                checkpoint(Protocol.State.HEADER_SIGN);
            case HEADER_SIGN:
                header.setSign(in.readByte());
                checkpoint(Protocol.State.HEADER_TYPE);
            case HEADER_TYPE:
                header.setType(in.readByte());
                checkpoint(Protocol.State.HEADER_INVOKER_ID);
            case HEADER_INVOKER_ID:
                header.setInvokeId(in.readLong());
                checkpoint(Protocol.State.HEADER_BODY_LENGTH);
            case HEADER_BODY_LENGTH:
                header.setBodyLength(in.readInt());
                checkpoint(Protocol.State.BODY);
            case BODY:
                byte[] bytes = new byte[checkBodyLength(header.getBodyLength())];
                in.readBytes(bytes);
                RemoteTransporter remoteTransporter = SerializerHolder.holder().get().deserialize(bytes, RemoteTransporter.class);
                if (remoteTransporter.getCode() != Protocol.Code.HEART_BEATS) {
                    out.add(remoteTransporter);
                }
                // 设置读指针断点，使下一次读取数据时能从MAGIC处开始读取。
                checkpoint(Protocol.State.HEADER_MAGIC);
                break;
            default:
                break;
        }
    }

    private void validateMagic(int magic) throws RemoteException {
        if (magic != Protocol.MAGIC) {
            throw new RemoteException("magic validate error...");
        }
    }

    private int checkBodyLength(int bodyLength) throws RemoteException {
        if (bodyLength >= MAX_BODY_LENGTH) {
            throw new RemoteException("body bytes to more");
        }
        return bodyLength;
    }
}
