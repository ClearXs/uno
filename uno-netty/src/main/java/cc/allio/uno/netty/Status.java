package cc.allio.uno.netty;

public enum Status {

    /**
     * 请求完成
     */
    OK((byte) 0x20, "OK"),

    /**
     * 服务调用错误
     */
    SERVICE_INVOKE_ERROR((byte) 0x21, "service invoke error"),

    /**
     * 服务未找到
     */
    SERVICE_NOT_FOUND((byte) 0x40, "service not found"),

    /**
     * 服务调用访问错误
     */
    SERVICE_ILLEGAL_ACCESS((byte) 0x41, "service illegal access");

    private final byte value;

    private final String description;

    Status(byte value, String description) {
        this.value = value;
        this.description = description;
    }

    public byte getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
