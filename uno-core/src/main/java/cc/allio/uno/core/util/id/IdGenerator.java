package cc.allio.uno.core.util.id;

/**
 * 分布式ID生成器
 *
 * @author j.x
 * @date 2022/1/26 09:46
 * @see SnowflakeIdWorker
 * @since 1.0
 */
public interface IdGenerator {

    /**
     * 获取下一个分布式ID
     *
     * @return Long类型的分布式Id
     */
    Long getNextId();

    /**
     * 获取下一个分布式ID
     *
     * @return String类型的分布式ID
     */
    String getNextIdAsString();

    /**
     * 获取下一个分布式ID
     *
     * @return 返回16进制的字符串
     */
    String toHex();

    /**
     * 获取默认ID生成器
     *
     * @return 默认ID生成器实例
     * @see InternalDataCenterIdGenerator
     */
    static IdGenerator defaultGenerator() {
        return InternalDataCenterIdGenerator.getInstance();
    }
}
