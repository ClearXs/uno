package cc.allio.uno.component.sequential.process;

import java.util.*;

import cc.allio.uno.component.sequential.BaseCompositeSequential;
import cc.allio.uno.component.sequential.Sequential;
import cc.allio.uno.component.sequential.context.DefaultSequentialContext;
import cc.allio.uno.component.sequential.context.SequentialContext;
import cc.allio.uno.core.bean.ObjectWrapper;
import cc.allio.uno.core.bus.MessageBus;
import cc.allio.uno.core.bus.Subscription;
import cc.allio.uno.core.bus.SubscriptionProperties;
import cc.allio.uno.core.bus.event.EmitEvent;
import cc.allio.uno.component.sequential.process.handle.AppendProcessHandler;
import cc.allio.uno.component.sequential.process.handle.ProcessHandler;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

/**
 * 在Spring容器下的时序数据处理器
 *
 * @author jw
 * @date 2021/12/13 16:46
 */
@Slf4j
public class SpringProcessor implements Processor, ApplicationContextAware, InitializingBean, DisposableBean {

    private ApplicationContext applicationContext;

    /**
     * 把从SPI获取到的{@link ProcessHandler}实例放入当前map对象的缓存中
     *
     * @key 时序数据类型
     * @value List的ProcessHandler类型
     */
    private final MultiValueMap<String, ProcessHandler> handlerCache = new LinkedMultiValueMap<>();

    /**
     * 把从SPI获取的{@link AppendProcessHandler}实例放入当前优先级队列中，不考虑延迟加载
     */
    private final Queue<AppendProcessHandler> appendProcessHandlers = new PriorityQueue<>();

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() {
        MessageBus<SequentialContext> bus = applicationContext.getBean(MessageBus.class);
        SubscriptionProperties subscriptionProperties = applicationContext.getBean(SubscriptionProperties.class);
        // 初始化加载追加处理器
        Flux.fromIterable(ServiceLoader.load(AppendProcessHandler.class))
                .doOnNext(appendProcessHandlers::add)
                // 订阅时序类型的数据
                .thenMany(Flux.fromIterable(Subscription.ofList(subscriptionProperties.getSequential())))
                .flatMap(bus::subscribe)
                .subscribe(node -> node.reply(EmitEvent.class, this::process));
    }

    @Override
    public void process(SequentialContext context) {
        Assert.notNull(context, "sequential context not null");
        Sequential sequential = context.getRealSequential();
        if (Objects.nonNull(sequential) && Objects.nonNull(sequential.getType())) {
            loaderProcessHandlerBySpi(sequential)
                    .map(tuple2 -> {
                        // 时序处理过程中，时序数据必须是一个唯一确定的sequential类型。所以CompositeSequential需要转换成指定的类型
                        if (sequential instanceof BaseCompositeSequential) {
                            ObjectWrapper wrapper = new ObjectWrapper(context.getClass());
                            wrapper.setForce("sequential", tuple2.getT1());
                        }
                        DefaultProcessPipeline pipeline =
                                new DefaultProcessPipeline(
                                        context,
                                        appendProcessHandlers.toArray(new AppendProcessHandler[]{}));
                        // 把当前处理时序数据处理器放入时序上下文中
                        context.putAttribute(DefaultSequentialContext.HANDLER, tuple2.getT2());
                        pipeline.adds(tuple2.getT2().toArray(new ProcessHandler[]{}));
                        return pipeline;
                    })
                    .doOnNext(ProcessPipeline::active)
                    .onErrorContinue((error, pipeline) -> log.error("pipeline execute error", error))
                    .subscribe();
        }
    }

    @Override
    public List<ProcessHandler> getProcessHandlers(String expectedType) {
        return Optional.ofNullable(handlerCache.get(expectedType))
                .orElse(Collections.emptyList());
    }

    /**
     * 如果当前缓存中没有存在时序数据的Handler，通过SPI加载当前时序类型的Handler并放入Cache中。<br/>
     *
     * @param sequential 时序数据，如果他是{@link BaseCompositeSequential}类型的话将会获取他子Sequential及其相关的Handler。
     * @return 返回多源的二元组，其中t1为{@link Sequential} t2是一个List的{@link ProcessHandler}
     */
    private Flux<Tuple2<Sequential, List<ProcessHandler>>> loaderProcessHandlerBySpi(Sequential sequential) {
        // 在初次进入会从SPI加载当前时序类型的Handler，随后将会从缓存中获取。
        return Mono.justOrEmpty(Optional.ofNullable(handlerCache.get(sequential.getType())))
                .flux()
                .switchIfEmpty(
                        Flux.fromIterable(ServiceLoader.load(ProcessHandler.class))
                                .groupBy(ProcessHandler::getType)
                                .filter(group -> sequential.getType().equals(group.key()))
                                .flatMap(group -> Mono.from(group.collectList()))
                                .doOnNext(list -> {
                                    list.forEach(ProcessHandler::init);
                                    handlerCache.put(sequential.getType(), list);
                                })
                                .onErrorContinue((error, o) -> log.error("process handle init error", error)))
                .map(processHandlers -> Tuples.of(sequential, processHandlers))
                // 如果当前获取的时序数据类型为空，那么他可能是组合的类型，如果是就根据他组合的类型来再次获取Handler
                .switchIfEmpty(composeProcessHandler(sequential));
    }

    /**
     * 获取组合的处理器数据流
     *
     * @param maybeCompose 可能是组合的Sequential对象
     * @return 返回多源的二元组，其中t1为{@link Sequential} t2是一个List的{@link ProcessHandler}
     */
    private Flux<Tuple2<Sequential, List<ProcessHandler>>> composeProcessHandler(Sequential maybeCompose) {
        if (maybeCompose instanceof BaseCompositeSequential) {
            BaseCompositeSequential compositeSequential = (BaseCompositeSequential) maybeCompose;
            return Flux
                    .fromIterable(compositeSequential.getCompositeMetadata())
                    .flatMap(this::loaderProcessHandlerBySpi);
        }
        return Flux.empty();
    }

    @Override
    public void destroy() {
        Flux.fromIterable(handlerCache.values())
                .doOnNext(list -> list.forEach(ProcessHandler::finish))
                .subscribe();
    }
}
