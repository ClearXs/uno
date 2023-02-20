package cc.allio.uno.component.sequential.washer;

import cc.allio.uno.component.sequential.Sequential;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Predicate;

/**
 * 清洗机器
 *
 * @author jiangwei
 * @date 2022/5/19 14:34
 * @since 1.0
 */
@Slf4j
public class WashMachine {
	private final Queue<Washer> wightWasher;
	private final Sequential sequential;

	WashMachine(Sequential sequential, List<Washer> washers) {
		this.wightWasher = new PriorityQueue<>((Comparator.comparingInt(Washer::order)));
		if (Objects.nonNull(washers)) {
			this.wightWasher.addAll(Lists.newArrayList(washers));
		}
		this.sequential = sequential;
	}

	/**
	 * <b>启动清洁装置</b></br>
	 * 按照优先级依次调用清洗器，不存在清洗器时默认返回true
	 */
	public boolean start() {
		boolean result = wightWasher.stream()
			.map(Washer::cleaning)
			.reduce(Predicate::and)
			.orElse(a -> true)
			.test(sequential);
		if (!result) {
			record();
		}
		return result;
	}

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
	 * 记录清洁过程的'脏物品'
	 */
	public void record() {
		// TODO 计划使用ES存储这部分脏数据
		log.warn("Wash Dirty data: {}", sequential);
	}
}
