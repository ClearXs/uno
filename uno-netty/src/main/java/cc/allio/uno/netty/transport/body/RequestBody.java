package cc.allio.uno.netty.transport.body;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 请求body
 *
 * @author jiangw
 * @date 2020/12/10 17:30
 * @since 1.0
 */
@Getter
@Setter
public class RequestBody extends AbstractBody {

    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(0);

    /**
     * 调用唯一id，以此对应消费者与提供者之间的请求
     */
    private final long invokeId;

    /**
     * 服务名
     */
    private String serviceName;

    /**
     * 调用的方法名
     */
    private String methodName;

    /**
     * 请求的参数
     */
    private Object[] parameters;

    /**
     * 请求期望返回的类型
     */
    private transient Class<?> expectedReturnType;

    public RequestBody() {
        invokeId = ID_GENERATOR.getAndIncrement();
    }

    @Override
    public String toString() {
        return "RequestBody{" +
                "invokeId=" + invokeId +
                ", serviceName='" + serviceName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parameters=" + Arrays.toString(parameters) +
                '}';
    }
}

