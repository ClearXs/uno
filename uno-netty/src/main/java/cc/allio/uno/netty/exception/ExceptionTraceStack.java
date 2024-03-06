package cc.allio.uno.netty.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义异常栈
 * @author jiangw
 * @date 2021/1/16 15:41
 * @since 1.0
 */
public class ExceptionTraceStack {

    private String cause;

    private final List<String> traceStacks;

    public ExceptionTraceStack() {
        this.traceStacks = new ArrayList<>();
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public List<String> getTraceStacks() {
        return traceStacks;
    }

    public void addTraceStack(String traceStack) {
        traceStacks.add(traceStack);
    }

    @Override
    public String toString() {
        return "ExceptionTraceStack{" +
                "cause='" + cause + '\'' +
                ", traceStack=" + traceStacks +
                '}';
    }
}
