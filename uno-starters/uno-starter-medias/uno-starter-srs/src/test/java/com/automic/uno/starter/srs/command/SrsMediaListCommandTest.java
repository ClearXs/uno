package cc.allio.uno.uno.starter.srs.command;

import cc.allio.uno.component.media.CommandContext;
import cc.allio.uno.component.media.MediaCache;
import cc.allio.uno.component.media.MediaException;
import cc.allio.uno.stater.srs.SrsMediaProperties;
import cc.allio.uno.stater.srs.command.SrsCommandController;
import cc.allio.uno.stater.srs.command.SrsMediaListCommand;
import cc.allio.uno.test.BaseCoreTest;
import cc.allio.uno.test.MockEnvironmentProperties;
import cc.allio.uno.test.env.RedisTestEnvironment;
import cc.allio.uno.test.env.TestSpringEnvironment;
import cc.allio.uno.test.env.TestSpringEnvironmentFacade;
import com.google.common.collect.Maps;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashMap;


/**
 * srs Media列表获取
 *
 * @author jiangwei
 * @date 2022/4/1 15:03
 * @since 1.0.6
 */
class SrsMediaListCommandTest extends BaseCoreTest {

    /**
     * Test Case: 测试获取Media列表
     */
    @Test
    void testMediaListCommand() throws MediaException, InterruptedException {
        SrsMediaProperties unoMediaProperties = new SrsMediaProperties();
        unoMediaProperties.setUrl("http://122.112.232.162:8086");
        SrsMediaListCommand command = new SrsMediaListCommand(0, 100);
        HashMap<String, Object> properties = Maps.newHashMap();
        properties.put(CommandContext.MEDIA_PROPERTY, unoMediaProperties);
        CommandContext context = new CommandContext(null, properties);
        command.execute(context)
                .flatMap(v -> Mono.just(context.containsKey(SrsMediaListCommand.MEDIA_LIST_TAG)))
                .as(StepVerifier::create)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void testCommandController() {
        SrsCommandController controller = getBean(SrsCommandController.class);
        SrsMediaListCommand command = new SrsMediaListCommand(0, 100);
        controller.touch(command)
                .map(context -> context.containsKey(SrsMediaListCommand.MEDIA_LIST_TAG))
                .as(StepVerifier::create)
                .expectNext(true)
                .verifyComplete();
    }

    @Override
    public TestSpringEnvironment supportEnv() {
        return new TestSpringEnvironmentFacade(
                new RedisTestEnvironment(MockEnvironmentProperties.mockRedisProperties())
        );
    }

    @Override
    protected void onRefreshComplete() throws Throwable {

    }

    @Override
    protected void onContextClose() throws Throwable {

    }

    @Override
    protected void onEnvBuild() {
        SrsMediaProperties unoMediaProperties = new SrsMediaProperties();
        unoMediaProperties.setUrl("http://122.112.232.162:8086");
        registerComponent(SrsCommandController.class, MediaCache.class);
        register(SrsMediaProperties.class, unoMediaProperties);
    }
}
