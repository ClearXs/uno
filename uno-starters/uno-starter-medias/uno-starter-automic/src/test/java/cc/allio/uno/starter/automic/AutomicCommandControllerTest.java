package cc.allio.uno.starter.automic;

import cc.allio.uno.component.media.command.CommandFactory;
import cc.allio.uno.component.media.command.MediaListCommand;
import cc.allio.uno.component.media.command.PlayCommand;
import cc.allio.uno.component.media.command.StopPlayCommand;
import cc.allio.uno.component.media.entity.Media;
import cc.allio.uno.starter.automic.command.AutomicCommandController;
import cc.allio.uno.test.BaseCoreTest;
import cc.allio.uno.test.Inject;
import cc.allio.uno.test.RunTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

/**
 * 测试Automic指令模型
 *
 * @author jiangwei
 * @date 2022/6/16 20:09
 * @since 1.0
 */
@Slf4j
@RunTest(components = UnoAutomicAutoConfiguration.class)
class AutomicCommandControllerTest extends BaseCoreTest {

    @Inject
    private AutomicCommandController controller;

    @Test
    void testGetMediaList() throws InterruptedException {
        controller.touch(CommandFactory.createCommand(MediaListCommand.class, null))
                .flatMap(commandContext -> {
                    List<Media> medias = commandContext.getOrThrows(MediaListCommand.MEDIA_LIST_TAG, List.class);
                    return Flux.fromIterable(medias);
                })
                .as(StepVerifier::create)
                .expectNextCount(1L)
                .verifyComplete();
    }

    @Test
    void testPlayMedia() throws InterruptedException {
        controller.touch(CommandFactory.createCommand(PlayCommand.class, "964"))
                .subscribe(commandContext -> {
                    Media media = commandContext.getOrThrows(PlayCommand.PLAY_DATA, Media.class);
                    log.info(media.toString());
                });

        Thread.sleep(10000L);
    }

    @Test
    void testStopPlay() throws InterruptedException {
        controller.touch(CommandFactory.createCommand(StopPlayCommand.class, "1005"))
                .subscribe();

        Thread.sleep(10000L);

    }
}
