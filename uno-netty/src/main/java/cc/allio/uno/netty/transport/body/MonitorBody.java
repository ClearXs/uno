package cc.allio.uno.netty.transport.body;

/**
 * 监听的body
 * @author j.x
 * @since 1.0
 */
public class MonitorBody implements Body {

    /**
     * 提供者的key
     */
    private String providerKey;

    /**
     * 服务的名称
     */
    private String serviceName;

    /**
     * 调用的方法
     */
    private MethodWrapper method;

    /**
     * 调用结果
     */
    private Object result;


    public String getProviderKey() {
        return providerKey;
    }

    public void setProviderKey(String providerKey) {
        this.providerKey = providerKey;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public MethodWrapper getMethodWrapper() {
        return method;
    }

    public void setMethodWrapper(MethodWrapper method) {
        this.method = method;
    }

    public static class MethodWrapper {

        private String name;

        private Class<?>[] parameterTypes;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Class<?>[] getParameterTypes() {
            return parameterTypes;
        }

        public void setParameterTypes(Class<?>[] parameterTypes) {
            this.parameterTypes = parameterTypes;
        }
    }

    @Override
    public String toString() {
        return "MonitorBody{" +
                "providerKey='" + providerKey + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", result=" + result +
                ", method=" + method +
                '}';
    }
}
