package cc.allio.uno.core.function.lambda;

import java.io.Serializable;

@FunctionalInterface
public interface ThrowingRunnable extends Serializable, LambdaMethod {

    /**
     * run
     */
    void run() throws Throwable;
}
