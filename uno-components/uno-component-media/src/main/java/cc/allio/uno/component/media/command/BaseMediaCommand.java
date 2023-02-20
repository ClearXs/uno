package cc.allio.uno.component.media.command;

import lombok.Getter;

/**
 * 定义Media构造参数
 *
 * @author jiangwei
 * @date 2022/11/25 15:26
 * @since 1.1.2
 */
@Getter
public abstract class BaseMediaCommand implements MediaCommand {

    /**
     * 多媒体数据code
     */
    private final String key;

    protected BaseMediaCommand(String key) {
        this.key = key;
    }
}
