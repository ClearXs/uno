package cc.allio.uno.component.flink.function;

import cc.allio.uno.component.flink.concurrent.JobRichContext;
import org.apache.flink.api.common.JobID;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.util.Objects;

/**
 * {@link RichSourceFunction}锁存器标识Function
 *
 * @author jiangwei
 * @date 2022/2/24 00:45
 * @see JobRichContext
 * @since 1.0
 */
@Deprecated
public abstract class BaseCountDownRichSourceFunction<T> extends RichSourceFunction<T> implements CountDownFunction {

    private transient JobID jobID;

    @Override
    public void open(Configuration parameters) throws Exception {
        afterOpen(parameters);
        RuntimeContext runtimeContext = getRuntimeContext();
        JobID jobId = runtimeContext.getJobId();
        jobID = jobId;
        JobRichContext.subtraction(jobId);
        JobRichContext.registerRich(jobId, this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BaseCountDownRichSourceFunction<?> that = (BaseCountDownRichSourceFunction<?>) o;
        return Objects.equals(jobID, that.jobID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobID);
    }
}
