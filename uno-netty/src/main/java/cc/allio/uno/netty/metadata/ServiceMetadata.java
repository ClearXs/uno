package cc.allio.uno.netty.metadata;

import lombok.Data;

import java.io.Serializable;

/**
 * 服务器元数据
 *
 * @author jiangw
 * @date 2020/11/28 13:58
 * @since 1.0
 */
@Data
public class ServiceMetadata implements Serializable {

    private String serviceProviderName;

    public ServiceMetadata(String serviceProviderName) {
        this.serviceProviderName = serviceProviderName;
    }
}
