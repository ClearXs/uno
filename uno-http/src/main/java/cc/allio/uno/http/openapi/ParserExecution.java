package cc.allio.uno.http.openapi;

import cc.allio.uno.core.util.ClassUtils;
import cc.allio.uno.core.util.Requires;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 解析器执行者
 *
 * @author jw
 * @date 2021/12/5 10:28
 */
public class ParserExecution {

    /**
     * 以缓存的形式存放着每一个存在的解析器
     */
    private final Map<String, Parser<?>> parserCache = new ConcurrentHashMap<>();

    /**
     * @param expectParser 期望的parser的Class对象
     * @see #execute(Class, String, ParserContext)
     */
    public <T> T execute(Class<? extends Parser<T>> expectParser, String unresolved, ParserContext context) {
        String genericClassName = ClassUtils.getSingleGenericClassName(expectParser);
        Object target = execute(expectParser.getName(), unresolved, context);
        return AccessController.doPrivileged((PrivilegedAction<T>) () -> {
            ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                if (genericClassName == null) {
                    return (T) target;
                }
                Class<?> instanceClass = Class.forName(genericClassName, false, currentClassLoader);
                return (T) instanceClass.cast(target);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return (T) target;
        });
    }

    /**
     * 进行解析工作，读取缓存中数据来获取解析器，如果不存在则抛出异常。
     *
     * @param expectParser 解析器的全限定名称
     * @param unresolved   等待解析的json字符串
     * @param context      解析器上下文对象
     * @throws NullPointerException 未找到解析器时抛出的异常
     */
    public Object execute(String expectParser, String unresolved, ParserContext context) {
        synchronized (this) {
            Parser<?> parser = Optional
                    .ofNullable(parserCache.get(expectParser))
                    .orElseThrow(() -> new NullPointerException(String.format("execution %s without in store.", expectParser)));
            // 解析前动作
            parser.preParse(context);
            // 解析时动作
            return parser.parse(unresolved, context);
        }
    }

    /**
     * @param parser 解析器实体类
     * @throws IllegalArgumentException 当parser为null时抛出异常
     * @see #register(String, Parser, ParserContext)
     */
    public void register(Parser<?> parser, ParserContext context) {
        Requires.isNotNull(parser, "parser");
        register(parser.getClass().getName(), parser, context);
    }

    /**
     * 向执行者缓存注册{@link Parser}，并触发{@link Parser#init(ParserContext)}生命周期<br/>
     * 如果解析器为null将会抛出{@link IllegalArgumentException}
     *
     * @param name    向缓存注册的Parser名称
     * @param parser  实例对象
     * @param context 解析器上下文
     * @throws IllegalArgumentException 当parser为null时，抛出异常
     * @throws NullPointerException     当name为null时，抛出异常
     */
    public void register(String name, Parser<?> parser, ParserContext context) {
        Requires.isNotNull(parser, "");
        parserCache.computeIfAbsent(name, key -> {
            parser.init(context);
            return parser;
        });
    }
}
