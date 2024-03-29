package cc.allio.uno.sequential.chain.handler;

import cc.allio.uno.sequnetial.context.SequentialContext;
import cc.allio.uno.sequnetial.process.handle.AppendProcessHandler;
import com.google.auto.service.AutoService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AutoService(AppendProcessHandler.class)
public class TestAppendProcessHandler implements AppendProcessHandler {
    @Override
    public void append(SequentialContext context) {
        log.info("{} execute append process handler", getClass().getSimpleName());
    }

    @Override
    public int order() {
        return 0;
    }
}
