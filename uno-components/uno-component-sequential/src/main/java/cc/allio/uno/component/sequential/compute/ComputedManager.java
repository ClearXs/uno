package cc.allio.uno.component.sequential.compute;

import cc.allio.uno.core.spi.Loader;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

/**
 * 计算实例管理器
 *
 * @author jiangwei
 * @date 2023/5/26 16:52
 * @since 1.1.4
 */
public class ComputedManager {

    /**
     * 计算存储实例
     */
    @Getter
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
