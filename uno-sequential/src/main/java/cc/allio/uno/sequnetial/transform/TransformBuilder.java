package cc.allio.uno.sequnetial.transform;

import cc.allio.uno.core.spi.Loader;
import cc.allio.uno.core.type.TypeManager;
import cc.allio.uno.sequnetial.context.SequentialContext;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 数据转换建造者
 *
 * @author j.x
 * @since 1.0
 */
public class TransformBuilder {

    private final List<Transducer> transducerGroup;
    /**
     * 转换的数据
     */
    private SequentialContext item;
    private final TypeManager typeManager;

    /**
     * 适用于转换的集合
     */
    private static final List<Transducer> TRANSDUCERS;

    static {
        TRANSDUCERS = Loader.loadList(Transducer.class);
    }

    private TransformBuilder(TypeManager typeManager) {
        transducerGroup = new ArrayList<>();
        this.typeManager = typeManager;
    }

    /**
     * 获取转换器控制器
     *
     * @return 转换器对象
     */
    public static TransformBuilder control(TypeManager typeManager) {
        return new TransformBuilder(typeManager);
    }

    /**
     * 添加一个数据变换器
     *
     * @param transducer transducer
     */
    public void addTransform(Transducer transducer) {
        transducerGroup.add(transducer);
    }

    /**
     * 放入数据
     *
     * @param item 转换的数据
     * @return 当前实例对象
     */
    public TransformBuilder buildItem(SequentialContext item) {
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
                if (typeManager.match(transducer.getType(), item.getRealSequential().getType())) {
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
