package cc.allio.uno.sequnetial.process;

import java.util.*;

import cc.allio.uno.core.bean.BeanWrapper;
import cc.allio.uno.core.bus.EventBusFactory;
import cc.allio.uno.core.bus.event.Node;
import cc.allio.uno.core.spi.Loader;
import cc.allio.uno.core.type.DefaultType;
import cc.allio.uno.core.type.Type;
import cc.allio.uno.core.type.TypeManager;
import cc.allio.uno.core.util.template.ExpressionTemplate;
import cc.allio.uno.core.util.template.Tokenizer;
import cc.allio.uno.sequnetial.BaseCompositeSequential;
import cc.allio.uno.sequnetial.Sequential;
import cc.allio.uno.sequnetial.context.DefaultSequentialContext;
import cc.allio.uno.sequnetial.context.SequentialContext;
import cc.allio.uno.sequnetial.process.handle.AppendProcessHandler;
import cc.allio.uno.sequnetial.process.handle.ProcessHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

/**
 * 默认的时序数据处理器。通过请求转发执行某个具体的{@link ProcessHandler}。
 * <p>外部调用{@link #process(SequentialContext)}把数据转发到主题{@link #DISPATCH_TOPIC}上，调用{@link #onDispatch(SequentialContext)}。</p>
 * <p>随后通过{@link TypeManager}判断时序数据是否存在于类型管理器中，如果存在则推送{@link #TOPIC_TEMPLATE}+时序数据类型的主题。在{@link #onProcess(SequentialContext)}在进行具体处理</p>
 *
 * @author j.x
 */
@Slf4j
public class DefaultProcessor implements Processor, InitializingBean, DisposableBean {

    // 请求转发主题
    private static final String DISPATCH_TOPIC = "/sequential/process/distributor";
    // 时序数据处理主题模板
    private static final String TOPIC_TEMPLATE = "/sequential/process/#{type}";
    private static final ExpressionTemplate TOPIC_TEMPLATE_PARSER = ExpressionTemplate.createTemplate(Tokenizer.HASH_BRACE);

    /**
     * 把从SPI获取到的{@link ProcessHandler}实例放入当前map对象的缓存中
     *
     * @key 时序数据类型
     * @value List的ProcessHandler类型
     */
    private final MultiValueMap<Type, ProcessHandler> handlerCache = new LinkedMultiValueMap<>();

    /**
     * 把从SPI获取的{@link AppendProcessHandler}实例放入当前优先级队列中，不考虑延迟加载
     */
    private final Queue<AppendProcessHandler> appendProcessHandlers = new PriorityQueue<>();

    private final TypeManager typeManager;

    public DefaultProcessor(TypeManager typeManager) {
        this.typeManager = typeManager;
    }

    @Override
    public void afterPropertiesSet() {
        // 初始化加载追加处理器
        appendProcessHandlers.addAll(Loader.loadList(AppendProcessHandler.class));
        // 订阅基础处理器，转发处理.
        EventBusFactory.<SequentialContext>current()
                .subscribeOnRepeatable(DISPATCH_TOPIC)
                .flatMap(Node::onNext)
                .subscribe(this::onDispatch);
    }


    @Override
    public void process(SequentialContext context) {
        if (context == null) {
            throw new IllegalArgumentException("sequential context not null");
        }
        EventBusFactory.current().publish(DISPATCH_TOPIC, context);
    }

    /**
     * 转发处理请求，尝试订阅对应的类型主题，订阅成功之后，对该主题发送数据
     *
     * @param context 时序数据上下文
     */
    private void onDispatch(SequentialContext context) {
        Sequential sequential = context.getRealSequential();
        String topic = TOPIC_TEMPLATE_PARSER.parseTemplate(TOPIC_TEMPLATE, "type", sequential.getType().getCode());
        Mono.justOrEmpty(sequential).filter(s -> typeManager.contains(s.getType()))
                .then(Mono.defer(() ->
                        // 判断是否存在指定主题，如果不存在则创建订阅关系，随后向该订阅关系发布时序数据
                        EventBusFactory.current().contains(topic)
                                .publishOn(Schedulers.boundedElastic())
                                .flatMap(c -> {
                                    if (Boolean.FALSE.equals(c)) {
                                        return EventBusFactory.<SequentialContext>current()
                                                .subscribeOnRepeatable(topic)
                                                .flatMap(Node::onNext)
                                                .flatMap(this::onProcess)
                                                .then(Mono.just(context));
                                    }
                                    return Mono.just(context);
                                })))
                .thenMany(Flux.defer(() -> EventBusFactory.current().publishOnFlux(topic, context)))
                .subscribe();
    }

    /**
     * 执行时序数据具体处理动作
     *
     * @param context 时序数据上下文
     */
    private Flux<DefaultProcessPipeline> onProcess(SequentialContext context) {
        Sequential sequential = context.getRealSequential();
        return loaderProcessHandlerBySpi(sequential)
                .map(tuple2 -> {
                    // 时序处理过程中，时序数据必须是一个唯一确定的sequential类型。所以CompositeSequential需要转换成指定的类型
                    if (sequential instanceof BaseCompositeSequential) {
                        BeanWrapper wrapper = new BeanWrapper(context.getClass());
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
                .onErrorContinue((error, pipeline) -> log.error("pipeline execute error", error));
    }

    @Override
    public List<ProcessHandler> getProcessHandlers(String expectedType) {
        return Optional.ofNullable(handlerCache.get(DefaultType.of(expectedType)))
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
                        Flux.fromIterable(Loader.load(ProcessHandler.class))
                                .groupBy(ProcessHandler::getType)
                                // Handler与时序数据进行类型匹配
                                .filter(group -> typeManager.match(group.key(), sequential.getType()))
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
