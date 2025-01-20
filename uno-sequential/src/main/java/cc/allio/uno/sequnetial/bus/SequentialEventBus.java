package cc.allio.uno.sequnetial.bus;

import cc.allio.uno.core.bus.BaseEventBus;
import cc.allio.uno.core.bus.Subscription;
import cc.allio.uno.core.bus.Topics;
import cc.allio.uno.sequnetial.SubscriptionProperties;
import cc.allio.uno.sequnetial.context.SequentialContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 基于Spring的事件总线
 *
 * @author j.x
 */
@Slf4j
public class SequentialEventBus extends BaseEventBus<SequentialContext> implements InitializingBean, DisposableBean, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private final Topics<SequentialContext> topics = new Topics<>();

    /**
     * bean初始化触发
     */
    @Override
    public void afterPropertiesSet() {
        SubscriptionProperties properties = applicationContext.getBean(SubscriptionProperties.class);
        Flux.fromIterable(Subscription.ofList(properties.getSequential()))
                .flatMap(subscription -> topics.link(subscription, this))
                .onErrorResume(error -> {
                    log.error("topic link on error return empty", error);
                    return Mono.empty();
                })
                .subscribe();
    }

    /**
     * bean销毁时触发
     */
    @Override
    public void destroy() {
        // 通知所有的Topic触发丢弃事件
        topics.deleteAll();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
