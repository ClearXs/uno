package cc.allio.uno.component.flink;

/**
 * 数据
 *
 * @author jiangwei
 * @date 2022/2/23 14:22
 * @since 1.0
 */
public interface Output<C> {

    /**
     * 输出数据
     *
     * @return 数据输出
     * @throws Exception 输出产生异常时抛出
     */
    C output() throws Exception;
}
