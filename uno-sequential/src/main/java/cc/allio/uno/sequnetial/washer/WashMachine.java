package cc.allio.uno.sequnetial.washer;

import cc.allio.uno.sequnetial.context.SequentialContext;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 清洗机器
 *
 * @author j.x
 * @date 2022/5/19 14:34
 * @since 1.0
 */
@Slf4j
public class WashMachine {

    private final Queue<Washer> wightWasher;
    private final SequentialContext context;

    WashMachine(SequentialContext sequentialContext, List<Washer> washers) {
        this.wightWasher = new PriorityQueue<>((Comparator.comparingInt(Washer::order)));
        if (Objects.nonNull(washers)) {
            this.wightWasher.addAll(Lists.newArrayList(washers));
        }
        this.context = sequentialContext;
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
    }

}
