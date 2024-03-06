package cc.allio.uno.netty.metadata;

import cc.allio.uno.netty.transport.UnresolvedAddress;
import io.netty.channel.Channel;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * 注册的元数据
 * @author jiangw
 * @date 2020/11/29 17:08
 * @since 1.0
 */
@Data
public class RegisterMetadata implements Serializable {

    /**
     * 服务提供者的名称
     */
    private final String serviceProviderName;

    /**
     * 服务的权重，负载均衡使用
     */
    private int weight;

    /**
     * 建议连接数，与客户端连接数
     */
    private int connCount;

    /**
     * 发布的服务
     */
    private Set<String> publishService;

    /**
     * 地址
     */
    private final UnresolvedAddress unresolvedAddress;

    /**
     * 所属的通道
     */
    private transient Channel channel;

    public RegisterMetadata(String providerServiceName, UnresolvedAddress socketAddress) {
        this(providerServiceName, 0, 4, socketAddress);
    }

    public RegisterMetadata(String serviceProviderName, int weight, int connCount, UnresolvedAddress unresolvedAddress) {
        this.serviceProviderName = serviceProviderName;
        this.weight = weight;
        this.connCount = connCount;
        this.unresolvedAddress = unresolvedAddress;
    }
}
