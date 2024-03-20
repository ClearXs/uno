package cc.allio.uno.core.util.id;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 内置数据中心的ID生成器。</br>
 * 通过在类加载时把{@link SnowflakeIdWorker}根据数据中心ID值创建好，通过CAS获取每一个数据中心的分布式ID
 *
 * @author j.x
 * @date 2022/1/26 09:43
 * @since 1.0
 */
public class InternalDataCenterIdGenerator implements IdGenerator {

    private static final InternalDataCenterIdGenerator ID_GENERATOR = new InternalDataCenterIdGenerator();

    /**
     * ID生成器
     */
    private static final SnowflakeIdWorker[] ID_WORKERS;

    /**
     * 数据中心ID范围为0~31
     */
    private static final int MAX_DATA_CENTER_ID = 31;

    /**
     * 期望指向下一个数据中心位置
     */
    private final AtomicInteger expect = new AtomicInteger(0);

    static {
        ID_WORKERS = new SnowflakeIdWorker[MAX_DATA_CENTER_ID];
        for (int i = 0; i < MAX_DATA_CENTER_ID; i++) {
            ID_WORKERS[i] = new SnowflakeIdWorker(0, i);
        }
    }

    private InternalDataCenterIdGenerator() {

    }

    @Override
    public synchronized Long getNextId() {
        int next = expect.getAndIncrement();
        if (next == MAX_DATA_CENTER_ID) {
            next = 0;
            expect.set(next);
        }
        return ID_WORKERS[next].nextId();
    }

    @Override
    public String getNextIdAsString() {
        return String.valueOf(getNextId());
    }

    @Override
    public String toHex() {
        return Long.toHexString(getNextId());
    }

    public static InternalDataCenterIdGenerator getInstance() {
        return ID_GENERATOR;
    }
}
