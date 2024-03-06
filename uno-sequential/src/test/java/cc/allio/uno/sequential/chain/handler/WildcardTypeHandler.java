package cc.allio.uno.sequential.chain.handler;

import cc.allio.uno.core.type.RegexType;
import cc.allio.uno.core.type.Type;
import cc.allio.uno.sequnetial.context.SequentialContext;
import cc.allio.uno.sequnetial.process.handle.AbstractProcessHandler;
import cc.allio.uno.sequnetial.process.handle.ProcessHandler;
import com.google.auto.service.AutoService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AutoService(ProcessHandler.class)
public class WildcardTypeHandler extends AbstractProcessHandler {
    @Override
    protected void doPreHandle(SequentialContext context) {
        log.info("doPreHandle match test*");
    }

    @Override
    protected void doHandle(SequentialContext context) {
        log.info("doHandle match test*");
    }

    @Override
    protected void doPostHandle(SequentialContext context) {
        log.info("doPostHandle match test*");
    }

    @Override
    public int order() {
        return 0;
    }

    @Override
    public Type getType() {
        return new RegexType("test*");
    }
}
