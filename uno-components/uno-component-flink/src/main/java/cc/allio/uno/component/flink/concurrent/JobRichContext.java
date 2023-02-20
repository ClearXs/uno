package cc.allio.uno.component.flink.concurrent;

import cc.allio.uno.component.flink.Input;
import cc.allio.uno.component.flink.Output;
import org.apache.flink.api.common.JobID;
import org.apache.flink.api.common.functions.RichFunction;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.deployment.TaskDeploymentDescriptor;
import org.apache.flink.runtime.jobmanager.slots.TaskManagerGateway;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * 基于JobId创建的计数器，它的目的是为了解决flink function序列化的问题所产生的</br>
 * <ol>
 *     <li>在Flink序列化中，无法维护没有实现序列化的对象，导致function中使用那些外部对象一定为null。解决这个问题是实现{@link RichFunction#open(Configuration)}来解决这个问题</li>
 *     <li>但又引出另外一个问题，触发{@link RichFunction#open(Configuration)}方法，是因为{@link TaskManagerGateway#submitTask(TaskDeploymentDescriptor, Time)}提交任务之后触发</li>
 *     <li>而提交任务是因为<b>JobManager</b>通过rpc进行触发，这个一个异步的过程。实现中，使用{@link StreamExecutionEnvironment#execute()}同步提交等待触发，
 *     但我需要的不是同步提交，而是异步提交，而且自定义function是一个同步阻塞的过程。不能满足</li>
 *     <li>为了解决这一个问题，引入这个类，这个类的目的是在提交数据后，等待任务是否已经提交完毕。它的实现原理是利用计数锁存器，当某一个function初始化完毕后计数减1，直到所有的计数完成，结束阻塞</li>
 * </ol>
 * 因为存在function的序列化，所以即使本地保存的function对象是没有被纳入Task中，这个类的另外一个目的就是为了获取这些对象而做的上下文存储器
 *
 * @author jiangwei
 * @date 2022/2/24 00:29
 * @since 1.0
 */
@Deprecated
public class JobRichContext {

    private JobRichContext() {

    }

    /**
     * 维护当前flink job的阻塞器
     */
    private static final Map<JobID, CountDownLatch> JOB_ID_BLOCKING = new ConcurrentHashMap<>();

    /**
     * 维护当前任务所使用的rich function
     */
    private static final Map<JobID, Set<RichFunction>> currentRich = new ConcurrentHashMap<>();

    /**
     * 向阻塞器缓存中存入job
     *
     * @param jobID jobId
     * @param count function数量
     * @return 锁存器对象实例
     */
    public static synchronized CountDownLatch registerCountDown(JobID jobID, Integer count) {
        CountDownLatch blocking = new CountDownLatch(count);
        JOB_ID_BLOCKING.put(jobID, blocking);
        return blocking;
    }

    /**
     * 减去当前Job的锁存器的计数值
     *
     * @param jobID jobID对象
     * @throws NullPointerException 不存在当前锁存器的Job
     */
    public static synchronized void subtraction(JobID jobID) {
        CountDownLatch blocking = JOB_ID_BLOCKING.get(jobID);
        if (blocking == null) {
            throw new NullPointerException(String.format("JobID %s countdown latch does not exist", jobID));
        }
        blocking.countDown();
    }

    /**
     * 向缓存中注册RichFunction
     *
     * @param jobID jobID对象
     * @param rich  RichFunction对象
     * @throws NullPointerException rich对象为空抛出
     */
    public static synchronized void registerRich(JobID jobID, RichFunction rich) {
        Set<RichFunction> richSet = currentRich.get(jobID);
        if (CollectionUtils.isEmpty(richSet)) {
            richSet = new HashSet<>();
            currentRich.put(jobID, richSet);
        }
        richSet.add(rich);
    }

    /**
     * 向Rich中获取Input
     *
     * @param jobID jobId对象
     * @return 空或者找到的Input对象
     */
    public static synchronized List<Input> getInputs(JobID jobID) {
        Set<RichFunction> richSet = currentRich.get(jobID);
        if (CollectionUtils.isEmpty(richSet)) {
            return Collections.emptyList();
        }
        return richSet.stream()
                .filter(v -> Input.class.isAssignableFrom(v.getClass()))
                .map(Input.class::cast)
                .collect(Collectors.toList());
    }

    /**
     * 向Rich中获取Output
     *
     * @param jobID jobID对象
     * @return 空或者找到的Output对象
     */
    public static synchronized List<Output> getOutputs(JobID jobID) {
        Set<RichFunction> richSet = currentRich.get(jobID);
        if (CollectionUtils.isEmpty(richSet)) {
            return Collections.emptyList();
        }
        return richSet.stream()
                .filter(v -> Output.class.isAssignableFrom(v.getClass()))
                .map(Output.class::cast)
                .collect(Collectors.toList());
    }
}
