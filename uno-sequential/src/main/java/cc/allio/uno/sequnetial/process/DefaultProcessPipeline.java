package cc.allio.uno.sequnetial.process;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

import cc.allio.uno.sequnetial.context.SequentialContext;
import cc.allio.uno.sequnetial.process.handle.AppendProcessHandler;
import cc.allio.uno.sequnetial.process.handle.ProcessHandler;
import cc.allio.uno.sequnetial.process.handle.ProcessHandlerChain;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 * 默认流水实例对象，handler对象采取优先级队列存储，
 *
 * @author jw
 * @date 2021/12/13 14:46
 */
@Slf4j
public class DefaultProcessPipeline implements ProcessPipeline {

    /**
     * 维护当前执行链的head
     */
    private ProcessHandlerChain head;

    /**
     * 处理链维护的上下文对象
     */
    private final SequentialContext context;

    /**
     * 追加到pipeline中，当{@link ProcessHandler}处理完成之后加载这个追加的处理器
     */
    private final List<AppendProcessHandler> appends;


    public DefaultProcessPipeline(SequentialContext context) {
        this.context = context;
        this.appends = new ArrayList<>();
    }

    public DefaultProcessPipeline(SequentialContext context, AppendProcessHandler... appendProcessHandlers) {
        this.context = context;
        this.appends = Arrays.asList(appendProcessHandlers);
    }

    @Override
    public void adds(ProcessHandler... handlers) {
        Flux<ProcessHandler> currentHandler = Flux.empty();
        if (head != null) {
            currentHandler = ((HandlerWrapper) head).toFlux();
        }
        currentHandler
                .mergeWith(Flux.fromArray(handlers))
                .sort(Comparator.comparingInt(ProcessHandler::order))
                .map(handler -> new HandlerWrapper(handler, null, null))
                .collectList()
                .doOnNext(list -> {
                    this.head = list.stream().findFirst().orElse(null);
                    AtomicReference<ProcessHandlerChain> pre = new AtomicReference<>(this.head);
                    list.forEach(chain -> {
                        ProcessHandlerChain preChain = pre.get();
                        if (!preChain.equals(chain)) {
                            chain.setPre(preChain);
                            ((HandlerWrapper) preChain).setNext(chain);
                            pre.set(chain);
                        }
                    });
                })
                .subscribe();
    }

    @Override
    public synchronized void active() {
        if (head != null) {
            head.process(context);
            Optional.ofNullable(appends)
                    .ifPresent(appendProcessHandlers ->
                            appendProcessHandlers.forEach(appendHandler -> appendHandler.append(context)));
        }
    }

    @Override
    public SequentialContext getContext() {
        return this.context;
    }

    @Getter
    static class HandlerWrapper implements ProcessHandlerChain {

        /**
         * 当前处理链中处理器
         */
        private final ProcessHandler handler;

        /**
         * 链中的上一个处理器
         */
        @Setter
        private ProcessHandlerChain pre;

        /**
         * 链中的下一个处理器
         */
        @Setter
        private ProcessHandlerChain next;

        public HandlerWrapper(ProcessHandler handler, ProcessHandlerChain pre, ProcessHandlerChain next) {
            this.handler = handler;
            this.pre = pre;
            this.next = next;
        }

        /**
         * @see ProcessHandler#filter()
         */
        @Override
        public void process(SequentialContext context) {
            Predicate<SequentialContext> predicate = handler.filter();
            boolean goon = predicate.test(context);
            if (goon) {
                handler.handle(context, next);
            } else {
                log.info("handler: {}, will be No processing", handler.getClass().getSimpleName());
            }
        }

        /**
         * 以当前链的向后搜索，直到next为空之间的数据转换为Flux数据流
         *
         * @return 当前搜索到的数据流
         */
        public Flux<ProcessHandler> toFlux() {
            Flux<ProcessHandler> handlerFlux = Flux.just(handler);
            ProcessHandlerChain expectNext = next;
            while (expectNext != null) {
                // 下一个链的处理器
                ProcessHandler nextHandler = ((HandlerWrapper) expectNext).getHandler();
                handlerFlux = handlerFlux.concatWithValues(nextHandler);
                expectNext = ((HandlerWrapper) expectNext).getNext();
            }
            return handlerFlux;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            HandlerWrapper that = (HandlerWrapper) o;
            return handler.equals(that.handler);
        }

        @Override
        public int hashCode() {
            return handler.hashCode();
        }
    }
}
