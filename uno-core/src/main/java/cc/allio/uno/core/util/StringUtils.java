package cc.allio.uno.core.util;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.util.template.internal.GenericTokenParser;
import cc.allio.uno.core.util.template.Tokenizer;
import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.lang.Nullable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 字符串工具
 *
 * @author j.x
 */
public class StringUtils extends org.springframework.util.StringUtils {

    private StringUtils() {
    }

    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

    public static boolean isBlank(final CharSequence cs) {
        if (cs == null) {
            return true;
        }
        int l = cs.length();
        if (l > 0) {
            for (int i = 0; i < l; i++) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }

    /**
     * 裁剪带有请求参数的url。<br/>
     *
     * @param url 请求url
     * @return 裁剪完没有带请求的地址
     */
    public static String cropUrl(String url) {
        return cropUrl(url, tuple -> {
        });
    }

    /**
     * 裁剪带有请求参数的url。<br/>
     * example：<a href="https://passport.baidu.com/passApi/js/wrapper.js?cdnversion=1638804235178&_=1638804234866">...</a><br/>
     * or<br/>
     * https://passport.baidu.com/passApi/{id}/{name}<br/>
     * <b>给定规则：</b><br/>
     * <i>1.请求链接中如果存在带有"?"并且后面跟有参数，那么把"url"裁剪成http://localhost/api, 其余的参数通过sink或者consumer进行消费</i><br/>
     * <i>2.请求链接中入股存在"{id}"的占位符格式，那么保留这个url。"id"这个参数通过sink或者consumer进行消费</i><br/>
     * <i>3.请求链接同时存在"?"与"{id}", 那么按照1与2的规则进行裁剪</i>
     *
     * @param url 请求url
     * @return 裁剪完没有带请求的地址
     */
    public static String cropUrl(String url, FluxSink<Tuple2<String, List<String>>> sink) {
        return cropUrl(url, sink::next);
    }

    /**
     * 裁剪带有请求参数的url。<br/>
     *
     * @param url               请求url
     * @param parameterConsumer consumer
     * @return 裁剪完没有带请求的地址
     */
    public static String cropUrl(String url, Consumer<Tuple2<String, List<String>>> parameterConsumer) {
        if (isBlank(url)) {
            return url;
        }
        // 获取地址中?在字符串的索引
        int question = url.indexOf(StringPool.QUESTION_MARK);
        // 找不到?的索引，则说明url中没有带有参数
        if (question < 0) {
            return cropPlaceholderUrl(url, parameterConsumer);
        }
        String realUrl = url.substring(0, question);
        // 获取参数的字符串
        String parameter = url.substring(question + 1);
        cropParameterUrl(parameter, parameterConsumer);
        return cropPlaceholderUrl(realUrl, parameterConsumer);
    }

    /**
     * 裁剪带有占位符的url地址
     *
     * @param placeholderUrl    占位符地址
     * @param parameterConsumer consumer
     * @return 返回原占位符地址
     */
    private static String cropPlaceholderUrl(String placeholderUrl, Consumer<Tuple2<String, List<String>>> parameterConsumer) {
        PlaceholderParameter.find(placeholderUrl)
                .forEach(placeholder -> parameterConsumer.accept(Tuples.of(placeholder.getName(), java.util.Collections.singletonList(placeholder.getName()))));
        return placeholderUrl;
    }

    /**
     * 裁剪带有参数的url地址
     *
     * @param parameterUrl      参数地址
     * @param parameterConsumer consumer
     */
    private static void cropParameterUrl(String parameterUrl, Consumer<Tuple2<String, List<String>>> parameterConsumer) {
        // 以&分割后，数据于key=value这样一对对形式保存在数组中
        Arrays.stream(parameterUrl.split(StringPool.AND)).forEach(pair -> {
            String[] keyValues = pair.split(StringPool.EQUALS);
            if (keyValues.length == 0) {
                return;
            }
            String key = keyValues[0];
            // value的值有可能是多个。比如key=value1=value2形式
            String[] values = new String[keyValues.length - 1];
            System.arraycopy(keyValues, 1, values, 0, keyValues.length - 1);
            if (parameterConsumer != null) {
                parameterConsumer.accept(Tuples.of(key, Arrays.asList(values)));
            }
        });
    }

    /**
     * 向url链接中拼接vars的内容<br/>
     * example：<a href="https://passport.baidu.com/passApi/js/wrapper.js?cdnversion=1638804235178&_=1638804234866">...</a><br/>
     * or<br/>
     * https://passport.baidu.com/passApi/{id}/{name}<br/>
     * <b>拼接分为两部分：</b><br/>
     * <i>1.占位符替换</i><br/>
     * <i>2.参数的拼接</i>
     *
     * @param templateUrl 模板url链接
     * @param vars        参数Map集合
     * @return 拼接完成后的url地址
     * @throws IllegalArgumentException 参数为空时抛出异常
     */
    public static String joinUrl(String templateUrl, Map<String, ?> vars) {
        Requires.isNotNulls(templateUrl, vars);
        if (CollectionUtils.isEmpty(vars)) {
            return templateUrl;
        }
        // 解析占位符个数
        List<PlaceholderParameter> placeholderParameters = PlaceholderParameter.find(templateUrl);
        // url增加'?'判断有两个，其一是vars大于url中占位符的个数，其二是链接中本无'?'
        if (placeholderParameters.size() < vars.size() && !templateUrl.contains(StringPool.QUESTION_MARK)) {
            templateUrl = templateUrl.concat(StringPool.QUESTION_MARK);
        }
        AtomicReference<String> join = new AtomicReference<>(templateUrl);
        Flux.fromIterable(placeholderParameters)
                .map(PlaceholderParameter::getName)
                // {guid}占位符的拼接
                .doOnNext(placeholder -> {
                    Object value = vars.get(placeholder);
                    vars.remove(placeholder);
                    String encodeOfParameter = String.valueOf(value);
                    encodeOfParameter = URLEncoder.encode(encodeOfParameter, StandardCharsets.UTF_8);
                    String replace = join.get().replace(StringPool.LEFT_BRACE.concat(placeholder).concat(StringPool.RIGHT_BRACE), encodeOfParameter);
                    join.set(replace);
                })
                .thenMany(Flux.fromIterable(vars.entrySet()))
                // ?par=1&par=2 请求参数的拼接
                .map(entity -> {
                    String encodeOfParameter = String.valueOf(entity.getValue());
                    encodeOfParameter = URLEncoder.encode(encodeOfParameter, StandardCharsets.UTF_8);
                    return entity.getKey().concat(StringPool.EQUALS).concat(encodeOfParameter);
                })
                .doOnNext(compose -> {
                    String oldUrl = join.get();
                    String newUrl = oldUrl.concat(compose).concat(StringPool.AMPERSAND);
                    join.set(newUrl);
                })
                .subscribe();
        String joinUrl = join.get();
        if (joinUrl.endsWith(StringPool.AMPERSAND)) {
            return joinUrl.substring(0, joinUrl.length() - 1);
        }
        return joinUrl;
    }

    /**
     * 获取url中基础的地址<br/>
     * example: <a href="https://passport.baidu.com/passApi/js/wrapper.js?cdnversion=1638804235178&_=1638804234866">...</a><br/>
     * result：passport.baidu.com
     *
     * @param templateUrl 请求链接
     * @return 解析完成的地址
     */
    public static Mono<String> getBaseUrl(String templateUrl) {
        return Flux.just(StringPool.HTTP, StringPool.HTTPS, StringPool.TCP, StringPool.MQTT)
                .filter(templateUrl::startsWith)
                .switchIfEmpty(Flux.just(templateUrl))
                .map(end -> {
                    String baseUrl = end;
                    if (!end.equals(templateUrl)) {
                        baseUrl = templateUrl.substring(end.length());
                    }
                    return Arrays.stream(baseUrl.split(StringPool.SLASH)).findFirst().orElse(baseUrl);
                })
                .single();
    }

    /**
     * 获取url中api的地址
     * example: <a href="https://passport.baidu.com/passApi/js/wrapper.js">...</a><br/>
     * result：/passApi/js/wrapper.js
     *
     * @param templateUrl 请求链接
     * @return 解析完成的地址
     */
    public static Mono<String> getApiUrl(String templateUrl) {
        return getBaseUrl(templateUrl)
                .map(baseUrl -> templateUrl.substring(templateUrl.indexOf(baseUrl) + baseUrl.length()));
    }

    /**
     * 获取url中主机地址<br/>
     * example: passport.baidu.com:8080
     * result: passport.baidu.com
     *
     * @param templateUrl 请求链接
     * @return 解析完成的地址
     */
    public static Mono<String> getHost(String templateUrl) {
        return getBaseUrl(templateUrl)
                .flatMapMany(baseUrl -> Flux.fromArray(baseUrl.split(StringPool.COLON)))
                .filter(meta -> !meta.chars().allMatch(Character::isDigit))
                .single();
    }

    /**
     * 获取url中主机端口<br/>
     * example: passport.baidu.com:8080
     * result: 8080
     *
     * @param templateUrl 请求链接
     * @return 解析完成的端口
     */
    public static Mono<Integer> getPort(String templateUrl) {
        return getBaseUrl(templateUrl)
                .flatMapMany(baseUrl -> Flux.fromArray(baseUrl.split(StringPool.COLON)))
                .filter(meta -> meta.chars().allMatch(Character::isDigit))
                .map(Integer::valueOf)
                .switchIfEmpty(Flux.just(0))
                .single();
    }

    @Data
    private static class PlaceholderParameter {

        /**
         * 占位符名称
         */
        private String name;

        /**
         * 所在url中index，去除'{}'之后
         */
        private int index;

        /**
         * 属于的url
         */
        private String key;

        /**
         * 从占位符链接中寻找占位符，封装成{@link PlaceholderParameter}对象
         *
         * @param placeholderUrl 占位符url
         * @return 找到的placeholder
         */
        private static List<PlaceholderParameter> find(String placeholderUrl) {
            return Arrays.stream(placeholderUrl.split(StringPool.SLASH))
                    .filter(unit -> unit.startsWith(StringPool.LEFT_BRACE) && unit.endsWith(StringPool.RIGHT_BRACE))
                    .map(unit -> unit.substring(1, unit.length() - 1))
                    .map(unit -> {
                        PlaceholderParameter placeholderParameter = new PlaceholderParameter();
                        placeholderParameter.setName(unit);
                        placeholderParameter.setIndex(placeholderUrl.indexOf(unit));
                        placeholderParameter.setKey(placeholderUrl);
                        return placeholderParameter;
                    })
                    .toList();
        }
    }

    /**
     * 同 log 格式的 format 规则
     * <p>
     * use: format("my name is {}, and i like {}!", "L.cm", "Java")
     *
     * @param message   需要转换的字符串
     * @param arguments 需要替换的变量
     * @return 转换后的字符串
     */
    public static String format(@Nullable String message, @Nullable Object... arguments) {
        // message 为 null 返回空字符串
        if (message == null) {
            return StringPool.EMPTY;
        }
        // 参数为 null 或者为空
        if (arguments == null || arguments.length == 0) {
            return message;
        }
        StringBuilder sb = new StringBuilder((int) (message.length() * 1.5));
        int cursor = 0;
        int index = 0;
        int argsLength = arguments.length;
        for (int start, end; (start = message.indexOf('{', cursor)) != -1 && (end = message.indexOf('}', start)) != -1 && index < argsLength; ) {
            sb.append(message, cursor, start);
            sb.append(arguments[index]);
            cursor = end + 1;
            index++;
        }
        sb.append(message.substring(cursor));
        return sb.toString();
    }

    /**
     * 格式化执行时间，单位为 ms 和 s，保留三位小数
     *
     * @param nanos 纳秒
     * @return 格式化后的时间
     */
    public static String format(long nanos) {
        if (nanos < 1) {
            return "0ms";
        }
        double millis = (double) nanos / (1000 * 1000);
        // 不够 1 ms，最小单位为 ms
        if (millis > 1000) {
            return String.format("%.3fs", millis / 1000);
        } else {
            return String.format("%.3fms", millis);
        }
    }

    /**
     * 将字符串的第一位转为小写
     *
     * @param str 需要转换的字符串
     * @return 转换后的字符串
     */
    public static String toLowerCaseFirstOne(String str) {
        if (Character.isLowerCase(str.charAt(0)))
            return str;
        else {
            char[] chars = str.toCharArray();
            chars[0] = Character.toLowerCase(chars[0]);
            return new String(chars);
        }
    }

    /**
     * 将字符串的第一位转为大写
     *
     * @param str 需要转换的字符串
     * @return 转换后的字符串
     */
    public static String toUpperCaseFirstOne(String str) {
        if (Character.isUpperCase(str.charAt(0)))
            return str;
        else {
            char[] chars = str.toCharArray();
            chars[0] = Character.toUpperCase(chars[0]);
            return new String(chars);
        }
    }

    /**
     * 字符串驼峰转下划线格式
     *
     * @param param 需要转换的字符串
     * @return 转换好的字符串
     */
    public static String camelToUnderline(String param) {
        if (isBlank(param)) {
            return StringPool.EMPTY;
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c) && i > 0) {
                sb.append(StringPool.UNDERSCORE);
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }

    /**
     * 字符串下划线转驼峰格式
     *
     * @param param 需要转换的字符串
     * @return 转换好的字符串
     */
    public static String underlineToCamel(String param) {
        if (isBlank(param)) {
            return StringPool.EMPTY;
        }
        String temp = param.toLowerCase();
        int len = temp.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = temp.charAt(i);
            if (c == StringPool.UNDERSCORE.toCharArray()[0]) {
                if (++i < len) {
                    sb.append(Character.toUpperCase(temp.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 按照给定的 Tokenizer参数对字符串进行切分. example：
     * <pre>
     *     text: user($p,$p) tokenizer: ()
     *     return $p,$p
     * </pre>
     *
     * @param text      原始文本数据
     * @param tokenizer Tokenizer
     * @return 切分完成后的Tokenizer
     */
    public static String[] split(String text, Tokenizer tokenizer) {
        List<String> tokens = Lists.newArrayList();
        GenericTokenParser tokenParser = new GenericTokenParser(tokenizer);
        tokenParser.parse(text, token -> {
            tokens.add(token);
            return token;
        });
        return tokens.toArray(new String[0]);
    }

}
