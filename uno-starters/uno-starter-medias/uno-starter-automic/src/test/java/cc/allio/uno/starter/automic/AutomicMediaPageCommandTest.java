package cc.allio.uno.starter.automic;

import cc.allio.uno.component.media.MediaException;
import cc.allio.uno.component.media.command.Command;
import cc.allio.uno.component.media.command.CommandFactory;
import cc.allio.uno.component.media.command.MediaPageCommand;
import cc.allio.uno.starter.automic.command.AutomicCommandController;
import cc.allio.uno.test.BaseCoreTest;
import cc.allio.uno.test.Inject;
import cc.allio.uno.test.RunTest;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

@RunTest(components = UnoAutomicAutoConfiguration.class)
class AutomicMediaPageCommandTest extends BaseCoreTest {

    @Inject
    private AutomicCommandController controller;

    @Test
    void testEmptyError() {
        assertThrows(MediaException.class, () -> CommandFactory.createCommand(MediaPageCommand.class, null));
    }

    @Test
    void testCreateCommand() {
        Command command = CommandFactory.createCommand(MediaPageCommand.class, 0, 1);
        assertNotNull(command);
    }

    @Test
    void testRequest() {
        Command command = CommandFactory.createCommand(MediaPageCommand.class, 0, 1);
        controller.getMediaPage((MediaPageCommand) command)
                .as(StepVerifier::create)
                .expectNextCount(1L)
                .verifyComplete();
    }

    @Test
    void testEmpty() {
        Command command = CommandFactory.createCommand(MediaPageCommand.class, 0, 1, "test");
        controller.getMediaPage((MediaPageCommand) command)
                .as(StepVerifier::create)
                .expectNextCount(0L)
                .verifyComplete();
    }
}
