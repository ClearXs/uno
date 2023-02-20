package cc.allio.uno.component.sequential.bus;

import cc.allio.uno.component.sequential.context.SequentialContext;
import cc.allio.uno.core.bus.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 基于Spring的消息总线
 *
 * @author jw
 * @date 2021/12/16 11:22
 */
@Slf4j
public class SequentialMessageBus extends BaseMessageBus<SequentialContext> implements InitializingBean, DisposableBean, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private final Topics<SequentialContext> topics = new Topics<>();

    /**
     * bean初始化触发
     */
    @Override
    public void afterPropertiesSet() {
        SubscriptionProperties properties = applicationContext.getBean(SubscriptionProperties.class);
        Flux.fromIterable(Subscription.ofList(properties.getSequential()))
                .flatMap(topics::link)
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
