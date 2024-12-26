package cc.allio.uno.netty.transport.body;

import cc.allio.uno.netty.exception.ExceptionTraceStack;

/**
 * 响应body
 * @author j.x
 * @since 1.0
 */
public class ResponseBody extends AbstractBody {

    /**
     * rpc调用的唯一id
     */
    private long invokeId;

    /**
     * 返回的状态
     */
    private byte status;

    /**
     * 返回的结果
     */
    private Object result;

    /**
     * 错误信息
     */
    private String error;

    /**
     * 错误异常栈
     */
    private ExceptionTraceStack exceptionTraceStack;

    public ResponseBody(long invokeId) {
        this.invokeId = invokeId;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public long getInvokeId() {
        return invokeId;
    }

    public void setInvokeId(long invokeId) {
        this.invokeId = invokeId;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public ExceptionTraceStack getExceptionTrace() {
        return exceptionTraceStack;
    }

    public void setExceptionTrace(ExceptionTraceStack exceptionTraceStack) {
        this.exceptionTraceStack = exceptionTraceStack;
    }

    @Override
    public String toString() {
        return "ResponseBody{" +
                "invokeId=" + invokeId +
                ", status=" + status +
                ", result=" + result +
                ", error='" + error + '\'' +
                '}';
    }
}

