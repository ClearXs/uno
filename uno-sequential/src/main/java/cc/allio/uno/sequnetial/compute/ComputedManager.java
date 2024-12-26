package cc.allio.uno.sequnetial.compute;

import cc.allio.uno.core.spi.Loader;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

/**
 * 计算实例管理器
 *
 * @author j.x
 * @since 1.1.4
 */
@Getter
public class ComputedManager {

    /**
     * 计算存储实例
     */
    private final List<Compute> computes;

    public ComputedManager() {
        this.computes = Lists.newArrayList();
        List<Compute> loadBySPI = Loader.loadList(Compute.class);
        computes.addAll(loadBySPI);
    }

    /**
     * 添加{@link Compute}实例
     *
     * @param compute compute
     */
    public void addCompute(@NonNull Compute compute) {
        computes.add(compute);
    }
}
