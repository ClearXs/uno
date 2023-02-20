package cc.allio.uno.component.media.command;

import cc.allio.uno.component.media.CommandContext;
import cc.allio.uno.component.media.MediaException;
import cc.allio.uno.component.media.entity.Media;
import cc.allio.uno.component.media.entity.Page;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 指令控制器，统一管理执行指令
 *
 * @author jiangwei
 * @date 2022/3/30 15:50
 * @since 1.0.6
 */
public interface CommandController {

    /**
     * 触发指令集
     *
     * @param commands 指令集合
     * @return 指令上下文
     * @throws MediaException 执行指令过程中的错误
     */
    Flux<CommandContext> touch(Command... commands) throws MediaException;

    /**
     * 执行播放指令操作，当存在多个指令时，这些指令集合都会全部执行，但记录的数据智能获取到最后一个
     *
     * @param playCommands 播放指令
     * @return 播放流地址
     * @throws MediaException 执行指令过程中的错误
     * @see CommandContext#getOrThrows(String, Class)
     */
    default Flux<Media> play(PlayCommand playCommands) throws MediaException {
        return touch(playCommands)
                .map(context -> context.getOrThrows(PlayCommand.PLAY_DATA, Media.class));
    }

    /**
     * 执行停止播放指令
     *
     * @param stopPlayCommands 停止播放指令集合
     * @return 空流
     * @throws MediaException 执行指令过程中出现的错误
     */
    default Flux<Void> stopPlay(StopPlayCommand... stopPlayCommands) throws MediaException {
        return touch(stopPlayCommands)
                .flatMap(context -> Mono.empty());
    }

    /**
     * 执行获取Media实体指令操作，当存在多个指令时，这些指令集合都会全部执行，但记录的数据只能获取到最后一个
     *
     * @param mediaCommand 媒体指令集合
     * @return 单流Media实例对象
     * @throws MediaException 执行指令过程中的错误
     * @see CommandContext#getOrThrows(String, Class)
     */
    default Mono<Media> getMedia(MediaCommand mediaCommand) throws MediaException {
        return touch(mediaCommand)
                .map(context -> context.getOrThrows(MediaCommand.MEDIA_TAG, Media.class))
                .single();
    }

    /**
     * 执行获取Media列表指令操作，当存在多个指令时，这些指令集合都会全部执行，但记录的数据智能获取到最后一个
     *
     * @param mediaListCommand 媒体指令集合
     * @return 多流Media实例集合对象
     * @throws MediaException 执行指令过程中的错误
     */
    default Flux<Media> getMediaList(MediaListCommand mediaListCommand) throws MediaException {
        return touch(mediaListCommand)
                .flatMap(context -> {
                    List<?> mediaList = context.getOrThrows(MediaListCommand.MEDIA_LIST_TAG, List.class);
                    return Flux.fromIterable(mediaList).cast(Media.class);
                });
    }

    /**
     * 执行获取Media分页指令操作，当存在多个指令时，这些指令集合都会全部执行，但记录的数据智能获取到最后一个
     *
     * @param mediaPageCommand 媒体指令集合
     * @return 多流Media实例集合对象
     * @throws MediaException 执行指令过程中的错误
     */
    default Mono<Page> getMediaPage(MediaPageCommand mediaPageCommand) throws MediaException {
        return touch(mediaPageCommand)
                .flatMap(context -> {
                    Page pageList = context.getOrThrows(MediaPageCommand.MEDIA_PAGE_TAG, Page.class);
                    return Mono.just(pageList).cast(Page.class);
                })
                .single();
    }

    /**
     * 控制设备，会根据指令，控制设备进行上、左、下、右、放大、缩小等动作
     *
     * @param mediaControlCommands 媒体指令集合
     * @return 返回的信息，是否成功
     * @throws MediaException 执行指令过程中的错误
     * @see CommandContext#getOrThrows(String, Class)
     */
    default Flux<String> controlMedia(MediaControlCommand... mediaControlCommands) throws MediaException {
        return touch(mediaControlCommands)
                .map(context -> context.getOrThrows(MediaControlCommand.MEDIA_CONTROL_TAG, String.class));
    }
}
