package cc.allio.uno.core.spi;

import cc.allio.uno.core.StringPool;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * 通过类加载器加载各个包下面的xx.factories文件，把他解析{key=interfaceType, [value=impl1, value=impl2]}形式
 * <p>参考自{@link org.springframework.core.io.support.SpringFactoriesLoader}</p>
 * factories文件配置实例：<br/>
 * cc.allio.uno.core.spi.Demo1=\<br/>
 * cc.allio.uno.core.spi.Demo1Impl,\<br/>
 * cc.allio.uno.core.spi.Demo1Impl2<br/>
 * <br/>
 * cc.allio.uno.core.spi.Demo2=\<br/>
 * cc.allio.uno.core.spi.Demo2Impl<br/>
 *
 * @author j.x
 * @since 1.0.6
 */
@Slf4j
public class FactoriesLoader {

    /**
     * factories文件路径
     */
    private final String factoriesPath;
    private final Map<String, LinkedHashSet<String>> cache = new HashMap<>();
    private static final String FACTORIES_SUFFIX = ".factories";

    public FactoriesLoader(String factoriesPath) {
        if (!factoriesPath.contains(FACTORIES_SUFFIX)) {
            throw new IllegalArgumentException(String.format("Factories file must include %s", FACTORIES_SUFFIX));
        }
        this.factoriesPath = factoriesPath;
    }

    /**
     * 加载factories并且获取
     *
     * @param name        目标接口的权限类名称
     * @param classLoader 加载当前路径下的类，可能是自定义的类加载器
     * @return 加载结果
     * @throws IOException 读取不到文件时抛出异常
     */
    public Set<String> loadFactories(String name, ClassLoader classLoader) throws IOException {
        if (classLoader == null) {
            classLoader = FactoriesLoader.class.getClassLoader();
        }
        LinkedHashSet<String> results = cache.get(name);
        if (results == null) {
            results = new LinkedHashSet<>();
            cache.put(name, results);
        } else {
            return results;
        }
        Enumeration<URL> resources = classLoader.getResources(factoriesPath);
        while (resources.hasMoreElements()) {
            Properties properties = new Properties();
            URL url = resources.nextElement();
            properties.load(getInputStream(url));
            String implNames = (String) properties.get(name);
            results.addAll(Arrays.asList(implNames.split(StringPool.COMMA)));
        }
        return results;
    }

    /**
     * 根据接口类型加载他的实现类型
     *
     * @param <T>           目标接口的类型
     * @param interfaceType 接口类型Class对象
     * @param classLoader   类加载器
     * @return 通过指定的factories文件，加载到的Set集合中
     * @throws IOException              当找不到factories文件时抛出异常
     * @throws IllegalArgumentException 当参数<code>interfaceType</code>不是接口类型时抛出
     */
    public <T> Set<Class<T>> loadFactoriesByType(Class<T> interfaceType, ClassLoader classLoader) throws IOException {
        if (!interfaceType.isInterface()) {
            throw new IllegalArgumentException("Must interface Type");
        }
        Set<String> implNames = loadFactories(interfaceType.getName(), classLoader);
        Set<Class<T>> factoriesTypes = new HashSet<>();
        for (String implName : implNames) {
            log.info("Load class [{}]", implName);
            try {
                factoriesTypes.add((Class<T>) classLoader.loadClass(implName));
            } catch (ClassNotFoundException e) {
                log.error("Load Class [{}] failed", implName, e);
            }
        }
        return factoriesTypes;
    }

    /**
     * 参考自{@link org.springframework.core.io.UrlResource#getInputStream()}
     */
    private InputStream getInputStream(URL url) throws IOException {
        URLConnection urlConnection = url.openConnection();
        try {
            return urlConnection.getInputStream();
        } catch (IOException e) {
            if (urlConnection instanceof HttpURLConnection) {
                ((HttpURLConnection) urlConnection).disconnect();
            }
            throw new IOException(e);
        }
    }
}
