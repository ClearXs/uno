package cc.allio.uno.core.spi;

import cc.allio.uno.core.util.ClassUtil;
import com.google.auto.service.AutoService;
import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * SPI加载器
 *
 * @author jiangwei
 * @date 2022/11/1 17:40
 * @since 1.0
 */
public abstract class Loader {

    /**
     * 给定指定的Class类型并且包含{@link AutoService}注解，那么将会转换程列表数据
     *
     * @param typeClass 类型class对象
     * @param <T>       泛型对象
     * @return list数据
     */
    public static <T> List<T> loadList(Class<T> typeClass) {
        ServiceLoader<T> loader = load(typeClass);
        Iterator<T> iterator = loader.iterator();
        return Lists.newArrayList(iterator);
    }

    /**
     * 根据类型获取SPI加载的对象
     *
     * @param typeClass 期望的类型
     * @param <T>       返回数据格式的范型
     * @return ServiceLoader实例对象
     */
    public static <T> ServiceLoader<T> load(Class<T> typeClass) {
        boolean containsAutoService = ClassUtil.containsAnnotation(typeClass, AutoService.class);
        if (!containsAutoService) {
            throw new NullPointerException("Given Type Not Contains @AutoService");
        }
        return ServiceLoader.load(typeClass);
    }

}
