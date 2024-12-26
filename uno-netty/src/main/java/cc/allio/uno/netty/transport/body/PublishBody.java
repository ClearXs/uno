package cc.allio.uno.netty.transport.body;

import cc.allio.uno.netty.transport.UnresolvedAddress;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * 发布的服务的信息体
 * @author j.x
 * @since 1.0
 */
@Data
public class PublishBody implements Body {

    /**
     * 地址信息
     */
    private UnresolvedAddress publishAddress;

    /**
     * 发布的服务名
     */
    private String serviceName;

    /**
     * 发布的服务名
     * value=包名
     */
    private List<String> publishServices;

    /**
     * 服务建议的连接数
     */
    private int connCount;

    /**
     * 服务的权重
     */
    private int wight;

    public void setPublishServices(String... publishServices) {
        this.publishServices = Arrays.asList(publishServices);
    }

}
