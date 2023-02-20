package cc.allio.uno.component.media;

import java.util.ServiceLoader;

/**
 * Media构建者静态工厂
 *
 * @author jiangwei
 * @date 2022/4/4 14:39
 * @since 1.0.6
 */
public class MediaBuilderFactory {

    private static MediaBuilder mediaBuilder;

    static {
        ServiceLoader<MediaBuilder> load = ServiceLoader.load(MediaBuilder.class);
        // 获取最后一个构建的SPI实例对象
        for (MediaBuilder builder : load) {
            mediaBuilder = builder;
        }
    }

    /**
     * 创建MediaBuilder对象
     *
     * @return MediaBuilder对象实例
     * @throws NullPointerException 当MediaFactory找不到SPI实例时抛出
     */
    public static MediaBuilder createMediaBuilder() {
        if (mediaBuilder == null) {
            throw new NullPointerException("Media Builder Not found 'SPI' Instance Object, Please Dose isn't exist");
        }
        return mediaBuilder;
    }
}
