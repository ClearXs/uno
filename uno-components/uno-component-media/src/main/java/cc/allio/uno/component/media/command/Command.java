package cc.allio.uno.component.media.command;

import cc.allio.uno.component.media.CommandContext;
import cc.allio.uno.component.media.MediaException;
import com.google.auto.service.AutoService;
import reactor.core.publisher.Mono;

/**
 * 多媒体指令，实现类通过SPI进行加载<br/>
 * 每一个指令需要符合单一职责的原则，不允许定义超过他作用的边界，比如{@link PlayCommand}表示只做播放功能相关的事情.<br/>
 * 在每个指令中存在一系列标识字符串，这些字符串的作用是用于告知如何从上下文中获取指定需要的数据.比如{@link PlayCommand#PLAY_DATA}
 *
 * @author jiangwei
 * @date 2022/3/30 11:21
 * @see AutoService
 * @since 1.0.6
 */
public interface Command {

    /**
     * 执行指令
     *
     * @param context 指令集上下文
     * @return 单流Void
     * @throws NullPointerException 执行需要的数据空时抛出异常
     * @throws MediaException       执行过程中，如遇到网络阻塞等情况发生时抛出异常
     */
    Mono<CommandContext> execute(CommandContext context) throws NullPointerException, MediaException;

}
