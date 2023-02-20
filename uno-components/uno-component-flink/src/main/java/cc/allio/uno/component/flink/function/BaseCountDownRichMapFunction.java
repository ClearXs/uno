package cc.allio.uno.component.flink.function;

import cc.allio.uno.component.flink.concurrent.JobRichContext;
import org.apache.flink.api.common.JobID;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.configuration.Configuration;

import java.util.Objects;

/**
 * {@link RichMapFunction}锁存器的标识接口
 *
 * @author jiangwei
 * @date 2022/2/24 00:55
 * @since 1.0
 */
@Deprecated
public abstract class BaseCountDownRichMapFunction<IN, OUT> extends RichMapFunction<IN, OUT> implements CountDownFunction {

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
        BaseCountDownRichMapFunction<?, ?> that = (BaseCountDownRichMapFunction<?, ?>) o;
        return Objects.equals(jobID, that.jobID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobID);
    }
}
