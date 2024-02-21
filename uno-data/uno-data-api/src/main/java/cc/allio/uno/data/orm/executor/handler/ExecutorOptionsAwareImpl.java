package cc.allio.uno.data.orm.executor.handler;

import cc.allio.uno.data.orm.executor.options.ExecutorOptions;

public abstract class ExecutorOptionsAwareImpl implements ExecutorOptionsAware {

    private ExecutorOptions executorOptions;

    @Override
    public void setExecutorOptions(ExecutorOptions executorOptions) {
        this.executorOptions = executorOptions;
    }

    protected ExecutorOptions obtainExecutorOptions() {
        return executorOptions;
    }
}
