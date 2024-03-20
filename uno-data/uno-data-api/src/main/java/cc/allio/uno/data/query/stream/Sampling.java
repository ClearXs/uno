package cc.allio.uno.data.query.stream;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 数据采样。
 * <p>基于随机采取指定的样本数据量</p>
 *
 * @author j.x
 * @date 2022/11/17 19:17
 * @since 1.1.1
 */
public class Sampling {

    /**
     * 样品
     */
    private final Collection<?> sample;

    /**
     * 抽取大小
     */
    private final int size;

    public Sampling(Collection<?> sample, int size) {
        this.sample = sample;
        this.size = size;
    }

    /**
     * 获取下一个连续抽样数据
     *
     * @return 样本数据集合数据
     */
    public Collection<?> getNext() {
        // 样本量为1或者采取样本量小于抽取大没有采集意义
        if (sample.size() <= 1 || sample.size() <= size) {
            return sample;
        }
        // 随机抽取数值与抽取量组合区间可能会超过样本量大小，当超过时则取0
        int start = ThreadLocalRandom.current().nextInt(sample.size()) - size;
        if (start <= 0) {
            start = 0;
        }
        int end = start + size;
        return Arrays.asList(ArrayUtils.subarray(sample.toArray(), start, end == sample.size() ? sample.size() - 1 : end));
    }

    /**
     * 获取下一个离散抽样数据
     *
     * @return 抽样集合数据
     */
    public Collection<?> getNextDiscrete() {
        // 样本量为1或者采取样本量小于抽取大没有采集意义
        if (sample.size() <= 1 || sample.size() <= size) {
            return sample;
        }
        Object[] arrayForSample = sample.toArray();
        return Stream.of(size)
                .map(ThreadLocalRandom.current()::nextInt)
                .map(randomNum -> arrayForSample[randomNum])
                .collect(Collectors.toList());
    }
}
