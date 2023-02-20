package cc.allio.uno.stater.ezviz;

import cc.allio.uno.component.media.event.MediaCallback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author heitianzhen
 * @date 2022/4/15 10:43
 */
@Slf4j
@RestController
@RequestMapping("/uno/ezviz/endpoint")
public class UnoEzvizEndpoint implements MediaCallback, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void onEvent(ApplicationEvent event) {
        applicationContext.publishEvent(event);
    }

    @Bean
    @ConditionalOnProperty(value = "automic.uno.media.endpoint", havingValue = "true", matchIfMissing = true)
    public UnoEzvizEndpoint endpoint() {
        return new UnoEzvizEndpoint();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }
}
