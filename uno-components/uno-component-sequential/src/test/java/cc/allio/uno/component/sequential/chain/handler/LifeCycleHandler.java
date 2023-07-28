package cc.allio.uno.component.sequential.chain.handler;

import cc.allio.uno.component.sequential.Sequential;
import cc.allio.uno.component.sequential.context.SequentialContext;
import cc.allio.uno.component.sequential.process.handle.AbstractProcessHandler;
import cc.allio.uno.component.sequential.process.handle.ProcessHandler;
import cc.allio.uno.core.type.DefaultType;
import cc.allio.uno.core.type.Type;
import com.google.auto.service.AutoService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AutoService(ProcessHandler.class)
public class LifeCycleHandler extends AbstractProcessHandler {

    @Override
    public void init() {
        log.info("execute init");
    }

    @Override
    protected void doPreHandle(SequentialContext context) {
        log.info("execute doPreHandle");
    }

    @Override
    protected void doHandle(SequentialContext context) {
        log.info("execute doHandle");
    }

    @Override
    protected void doPostHandle(SequentialContext context) {
        log.info("execute doPostHandle");
    }

    @Override
    public int order() {
        return 0;
    }

    @Override
    public Type getType() {
        return DefaultType.of( "life cycle");
    }

    @Override
    public Class<? extends Sequential> getTypeClass() {
        return null;
    }

    @Override
    public void finish() {
        log.info("execute finish");
    }
}
