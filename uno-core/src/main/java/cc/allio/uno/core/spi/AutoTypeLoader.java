package cc.allio.uno.core.spi;

import cc.allio.uno.core.type.Type;
import com.google.auto.service.AutoService;
import com.google.common.collect.Lists;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;

import java.util.*;
import java.util.stream.Collectors;

/**
 * {@link AutoService}与{@link Type}数据类型加载单例对象，提供的静态方法获取实例数据不做缓存
 *
 * @author j.x
 * @date 2022/5/20 10:42
 * @since 1.0
 */
public abstract class AutoTypeLoader extends Loader {

    /**
     * 从SPI中加载实例对象返回Map结构
     *
     * @param typeClass 期望的类型
     * @param <T>       返回数据格式的范型
     * @return 以范型<K>为Key，范型<T>为value的数据
     * @throws NullPointerException 提供的入参没有被{@link AutoService}注解注释抛出该异常
     */
    public static <T extends Type> Map<String, List<T>> loadByTypeToMap(Class<T> typeClass) {
        ServiceLoader<T> loader = load(typeClass);
        Iterator<T> iterator = loader.iterator();
        return Lists.newArrayList(iterator)
                .stream()
                .collect(Collectors.groupingBy(Type::getCode));
    }

    /**
     * 从SPI中加载实例对象返回List结构
     *
     * @param typeClass 期望的类型
     * @param <T>       返回数据格式的范型
     * @return 以范型<K>为Key，范型<T>为value的数据
     * @throws NullPointerException 提供的入参没有被{@link AutoService}注解注释抛出该异常
     */
    public static <T> List<T> loadToList(Class<T> typeClass) {
        ServiceLoader<T> loader = load(typeClass);
        Iterator<T> iterator = loader.iterator();
        return new ArrayList<>(Lists.newArrayList(iterator));
    }

    /**
     * 从SPI中加载实例对象返回GroupFlux结构
     *
     * @param typeClass 期望的类型
     * @param <T>       返回数据格式的范型
     * @return 以范型<K>为Key，范型<T>为value的数据
     * @throws NullPointerException 提供的入参没有被{@link AutoService}注解注释抛出该异常
     */
    public static <T extends Type> Flux<GroupedFlux<String, T>> loadByTypeToGroupFlux(Class<T> typeClass) {
        ServiceLoader<T> loader = load(typeClass);
        return Flux.fromIterable(loader)
                .groupBy(Type::getCode);
    }

    /**
     * 从SPI中加载实例对象返回Flux结构
     *
     * @param typeClass 期望的类型
     * @param <T>       返回数据格式的范型
     * @return 以范型<K>为Key，范型<T>为value的数据
     * @throws NullPointerException 提供的入参没有被{@link AutoService}注解注释抛出该异常
     */
    public static <T> Flux<T> loadToFlux(Class<T> typeClass) {
        ServiceLoader<T> loader = load(typeClass);
        return Flux.fromIterable(loader);
    }

}
