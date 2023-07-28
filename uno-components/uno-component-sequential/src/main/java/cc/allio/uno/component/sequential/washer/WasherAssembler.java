package cc.allio.uno.component.sequential.washer;

import cc.allio.uno.component.sequential.context.SequentialContext;
import cc.allio.uno.core.spi.Loader;
import cc.allio.uno.core.type.TypeManager;
import com.google.common.collect.Lists;

import java.util.*;

/**
 * 清洁器装配器
 *
 * @author jiangwei
 * @date 2022/5/19 14:33
 * @since 1.0
 */
public class WasherAssembler {

    private final TypeManager typeManager;
    /**
     * 物品清洗装置
     */
    private static final List<Washer> ITEM_WASHERS;

    static {
        ITEM_WASHERS = Loader.loadList(Washer.class);
    }

    private WasherAssembler(TypeManager typeManager) {
        this.typeManager = typeManager;
    }

    /**
     * 创建清洗装置的主板
     *
     * @return 清洗装置主板L
     */
    public static WasherAssembler motherBoard(TypeManager typeManager) {
        return new WasherAssembler(typeManager);
    }

    /**
     * 清洁器组
     */
    private final List<Washer> washers = new ArrayList<>();
    /**
     * 待清洗的未知物品
     */
    private SequentialContext unknownItem;

    /**
     * 放入清洗物品
     *
     * @param context 清洗物品
     * @return 当前对象
     */
    public WasherAssembler pushItem(SequentialContext context) {
        this.unknownItem = context;
        return this;
    }

    /**
     * 装配配置清洁器
     *
     * @return 当前对象
     */
    public WasherAssembler assembleDefault() {
        washers.add(new DefaultWasher());
        return this;
    }

    /**
     * 根据清洁数据分配清洁器
     *
     * @return 当前对象
     */
    public WasherAssembler assembleAssignWasher() {
        if (Objects.nonNull(unknownItem)) {
            for (Washer washer : ITEM_WASHERS) {
                if (typeManager.match(washer.getType(), unknownItem.getRealSequential().getType())) {
                    washers.add(washer);
                }
            }
        }
        return this;
    }

    public WasherAssembler assembleCustom(Washer... washers) {
        this.washers.addAll(Lists.newArrayList(washers));
        return this;
    }

    /**
     * 安装清洗装置
     *
     * @return 清洗装置实例
     * @throws IllegalArgumentException 不存在清洗物品时抛出
     */
    public WashMachine install() {
        if (Objects.isNull(unknownItem)) {
            throw new IllegalArgumentException("No cleaning item");
        }
        return new WashMachine(unknownItem, washers);
    }
}
