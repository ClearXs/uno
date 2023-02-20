package cc.allio.uno.component.sequential.transform;

import cc.allio.uno.component.sequential.Sequential;
import cc.allio.uno.core.spi.AutoTypeLoader;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 数据转换建造者
 *
 * @author jiangwei
 * @date 2022/5/20 12:03
 * @since 1.0
 */
public class TransformBuilder {

    /**
     * 转换的数据
     */
    private Sequential item;

    private final List<Transducer> transducerGroup;

    /**
     * 适用于转换的集合
     */
    private static final List<Transducer> TRANSDUCERS;

    static {
        TRANSDUCERS = AutoTypeLoader.loadToList(Transducer.class);
    }


    private TransformBuilder() {
        transducerGroup = new ArrayList<>();
    }

    /**
     * 获取转换器控制器
     *
     * @return 转换器对象
     */
    public static TransformBuilder control() {
        return new TransformBuilder();
    }

    /**
     * 放入数据
     *
     * @param item 转换的数据
     * @return 当前实例对象
     */
    public TransformBuilder buildItem(Sequential item) {
        this.item = item;
        return this;
    }

    /**
     * 构建转换器
     *
     * @return 当前实例对象
     */
    public TransformBuilder buildTransducer() {
        if (Objects.nonNull(item)) {
            for (Transducer transducer : TRANSDUCERS) {
                if (transducer.contains(item.getType())) {
                    transducerGroup.add(transducer);
                }
            }
        }
        return this;
    }

    public TransformBuilder buildCustom(Transducer... transducers) {
        transducerGroup.addAll(Lists.newArrayList(transducers));
        return this;
    }

    /**
     * 构建
     *
     * @return 当前对象
     */
    public List<Transducer> build() {
        return transducerGroup;
    }
}
