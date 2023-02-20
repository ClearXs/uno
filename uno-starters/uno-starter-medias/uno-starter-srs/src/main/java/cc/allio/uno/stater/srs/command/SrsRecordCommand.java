package cc.allio.uno.stater.srs.command;

import cc.allio.uno.component.media.CommandContext;
import cc.allio.uno.component.media.MediaException;
import cc.allio.uno.component.media.command.RecordCommand;
import reactor.core.publisher.Mono;

/**
 * Srs 流媒体记录
 *
 * @author jiangwei
 * @date 2022/3/30 23:27
 * @since 1.0.6
 */
public class SrsRecordCommand implements RecordCommand {
    @Override
    public Mono<CommandContext> execute(CommandContext context) throws NullPointerException, MediaException {
        return Mono.empty();
    }
}
