package cc.allio.uno.netty.model;

import cc.allio.uno.core.util.id.IdGenerator;
import cc.allio.uno.netty.Protocol;

public class RemoteTransporter extends Byte {

    /**
     * 传输的类型{@link Protocol.Code}
     */
    private byte code;


    private long unique = IdGenerator.defaultGenerator().getNextId();

    /**
     * 1.请求消息体
     * 2.响应消息体
     * 3.发布消息体
     * 4.订阅消息体...
     */
    private cc.allio.uno.netty.transport.body.Body body;

    /**
     * 请求or响应{@link Protocol.TransportType}
     */
    private byte transporterType;

    public byte getCode() {
        return code;
    }

    public void setCode(byte code) {
        this.code = code;
    }

    public long getUnique() {
        return unique;
    }

    public void setUnique(long unique) {
        this.unique = unique;
    }

    public cc.allio.uno.netty.transport.body.Body getBody() {
        return body;
    }

    public void setBody(cc.allio.uno.netty.transport.body.Body body) {
        this.body = body;
    }

    public byte getTransporterType() {
        return transporterType;
    }

    public void setTransporterType(byte transporterType) {
        this.transporterType = transporterType;
    }

    @Override
    public String toString() {
        return "RemoteTransporter{" +
                "code=" + code +
                ", unique=" + unique +
                ", body=" + body +
                ", transporterType=" + transporterType +
                '}';
    }

    /**
     * 创建请求
     *
     * @param code
     * @param body
     * @return
     */
    public static RemoteTransporter createRemoteTransporter(byte code, cc.allio.uno.netty.transport.body.Body body) {
        RemoteTransporter remoteTransporter = new RemoteTransporter();
        remoteTransporter.setTransporterType(Protocol.TransportType.REMOTE_REQUEST);
        remoteTransporter.setCode(code);
        remoteTransporter.setBody(body);
        return remoteTransporter;
    }

    /**
     * 创建响应
     *
     * @param code
     * @param body
     * @param unique
     * @return
     */
    public static RemoteTransporter createRemoteTransporter(byte code, cc.allio.uno.netty.transport.body.Body body, long unique) {
        RemoteTransporter transporter = new RemoteTransporter();
        transporter.setTransporterType(Protocol.TransportType.REMOTE_RESPONSE);
        transporter.setCode(code);
        transporter.setBody(body);
        transporter.setUnique(unique);
        return transporter;
    }

    /**
     * @param code
     * @param body
     * @param unique
     * @param transporterType
     * @return
     */
    public static RemoteTransporter createRemoteTransporter(byte code, cc.allio.uno.netty.transport.body.Body body, long unique, byte transporterType) {
        RemoteTransporter transporter = new RemoteTransporter();
        transporter.setTransporterType(transporterType);
        transporter.setCode(code);
        transporter.setBody(body);
        transporter.setUnique(unique);
        return transporter;
    }

    /**
     * 出现一些未知的错误时候，快速发送失败的信息
     *
     * @param unique
     * @return
     */
    public static RemoteTransporter failedResponse(long unique, Throwable cause) {
        RemoteTransporter remoteTransporter = new RemoteTransporter();
        remoteTransporter.setUnique(unique);
        remoteTransporter.setTransporterType(Protocol.TransportType.REMOTE_RESPONSE);
        remoteTransporter.setCode(Protocol.Code.FAILED);
        cc.allio.uno.netty.transport.body.FailBody failBody = new cc.allio.uno.netty.transport.body.FailBody(cause);
        remoteTransporter.setBody(failBody);
        return remoteTransporter;
    }
}
