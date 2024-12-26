package cc.allio.uno.sequnetial.washer;

import cc.allio.uno.core.reactive.BufferRate;
import cc.allio.uno.data.orm.executor.AggregateCommandExecutor;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.CommandExecutorFactory;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import cc.allio.uno.sequnetial.Sequential;
import cc.allio.uno.sequnetial.context.SequentialContext;
import com.google.common.collect.Lists;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 清洗机器
 *
 * @author j.x
 * @since 1.0
 */
@Slf4j
public class WashMachine {

    private FluxSink<WasherRecord> recorder;
    private final Disposable recorderDisposable;

    private final Queue<Washer> wightWasher;
    private final SequentialContext context;

    WashMachine(SequentialContext sequentialContext, List<Washer> washers) {
        this.wightWasher = new PriorityQueue<>((Comparator.comparingInt(Washer::order)));
        if (Objects.nonNull(washers)) {
            this.wightWasher.addAll(Lists.newArrayList(washers));
        }
        this.context = sequentialContext;
        this.recorderDisposable =
                BufferRate.create(Flux.<WasherRecord>create(sink -> recorder = sink))
                        .doOnNext(records -> {
                            AggregateCommandExecutor sqlExecutor = CommandExecutorFactory.getDSLExecutor(ExecutorKey.ELASTICSEARCH);
                            if (sqlExecutor != null) {
                                sqlExecutor.batchInsertPojos(records);
                            }
                        })
                        .onErrorContinue((err, o) -> log.error("the save wash record is failed", err))
                        .subscribe();
    }

    /**
     * <b>启动清洁装置</b></br>
     * 按照优先级依次调用清洗器，不存在清洗器时默认返回true
     */
    public boolean start() {
        // 满足的条件集合
        List<Washer> satisfy = Lists.newArrayList();
        boolean result = true;
        for (Washer washer : wightWasher) {
            boolean test = washer.cleaning().test(context);
            if (!test) {
                satisfy.add(washer);
            }
            result = result && test;
        }
        if (!result) {
            record(satisfy);
        }
        return result;
    }

    /**
     * 清楚清洗器
     */
    public void stop() {
        this.wightWasher.clear();
        if (recorderDisposable != null && !recorderDisposable.isDisposed()) {
            recorderDisposable.dispose();
        }
    }

    /**
     * 判断当前清洗装置中是否包含目标的的清洗器
     *
     * @param target 目标清洗class对象
     * @return true存在，false不存在
     */
    public boolean contains(Class<? extends Washer> target) {
        if (Objects.isNull(target)) {
            throw new IllegalArgumentException("Target Washer Class type is null");
        }
        for (Washer washer : wightWasher) {
            if (washer.getClass().isAssignableFrom(target)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 记录清洁过程的'脏物品'，采用es存储数据。记录达到清洗的条件与对应清洗值
     *
     * @param conditions 达到清洗的条件
     */
    private void record(List<Washer> conditions) {
        // 构建WasherRecord
        WasherRecord washerRecord = new WasherRecord();
        Sequential sequential = context.getRealSequential();
        washerRecord.setType(sequential.getOriginType().getCode());
        Map<String, String> conditionsDescription = conditions.stream().collect(Collectors.toMap(v -> v.getClass().getSimpleName(), Washer::description));
        washerRecord.setConditions(conditionsDescription);
        washerRecord.setProperties(sequential.getValues());
        recorder.next(washerRecord);
    }

    /**
     * 清洗记录
     */
    @Data
    @Table(name = "wash_${type}")
    public static class WasherRecord {

        @Id
        private Long id;

        /**
         * 时序数据类型
         */
        private String type;

        /**
         * 条件集的描述
         */
        private Map<String, String> conditions;

        /**
         * 记录属性
         */
        private Map<String, Object> properties;
    }
}
