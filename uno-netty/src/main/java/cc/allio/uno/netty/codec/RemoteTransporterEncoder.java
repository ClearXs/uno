package cc.allio.uno.netty.codec;

import cc.allio.uno.netty.Protocol;
import cc.allio.uno.netty.model.RemoteTransporter;
import cc.allio.uno.core.serializer.SerializerHolder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class RemoteTransporterEncoder extends MessageToByteEncoder<RemoteTransporter> {

    @Override
    protected void encode(ChannelHandlerContext ctx, RemoteTransporter msg, ByteBuf out) throws Exception {
        // 对象序列化
        byte[] bytes = SerializerHolder.holder().get().serialize(msg);
        msg.setBytes(bytes);
        // 发送请求头
        out.writeInt(Protocol.MAGIC)
                .writeByte(msg.getTransporterType())
                .writeByte(msg.getCode())
                .writeLong(msg.getUnique())
                .writeInt(msg.length())
                .writeBytes(bytes);
    }
}
