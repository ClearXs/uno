package cc.allio.uno.component.media.command;

import lombok.Getter;

/**
 * 定义Page构造参数
 *
 * @author jiangwei
 * @date 2022/11/25 15:24
 * @since 1.1.2
 */
@Getter
public abstract class BaseMediaPageCommand implements MediaPageCommand {

    /**
     * 分页页码
     */
    private final Integer start;

    /**
     * 分页数量
     */
    private final Integer count;

    protected BaseMediaPageCommand(Integer start, Integer count) {
        // 参数纠错
        if (start == null || start < 0) {
            this.start = 0;
        } else {
            this.start = start;
        }
        if (count == null || count < 0) {
            this.count = Integer.MAX_VALUE;
        } else {
            this.count = count;
        }
    }

}
