package cc.allio.uno.component.sequential.washer;

import cc.allio.uno.component.sequential.Sequential;
import cc.allio.uno.core.spi.AutoTypeLoader;
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

    /**
     * 物品清洗装置
     */
    private static final List<Washer> ITEM_WASHERS;

    static {
        ITEM_WASHERS = AutoTypeLoader.loadToList(Washer.class);
    }

    private WasherAssembler() {

    }

    /**
     * 创建清洗装置的主板
     *
     * @return 清洗装置主板L
     */
    public static WasherAssembler motherBoard() {
        return new WasherAssembler();
    }

    /**
     * 清洁器组
     */
    private final List<Washer> washers = new ArrayList<>();


    /**
     * 待清洗的未知物品
     */
    private Sequential unknownItem;

    /**
     * 放入清洗物品
     *
     * @param sequential 清洗物品
     * @return 当前对象
     */
    public WasherAssembler pushItem(Sequential sequential) {
        this.unknownItem = sequential;
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
            for (Washer itemWasher : ITEM_WASHERS) {
                if (itemWasher.contains(unknownItem.getType())) {
                    washers.add(itemWasher);
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
