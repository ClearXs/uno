package cc.allio.uno.component.media.command;

/**
 * 停止播放指令，由每个模块进行具体实现
 *
 * @author jiangwei
 * @date 2022/6/16 17:31
 * @since 1.0
 */
public interface StopPlayCommand extends Command {

    String STOP_PLAY_KEY = "STOP_PLAY_KEY";
}
