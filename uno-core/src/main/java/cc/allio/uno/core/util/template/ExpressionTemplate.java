package cc.allio.uno.core.util.template;

import cc.allio.uno.core.util.IoUtils;
import com.google.common.collect.Maps;
import org.springframework.core.io.UrlResource;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple4;
import reactor.util.function.Tuple6;
import reactor.util.function.Tuples;

import javax.annotation.processing.FilerException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

/**
 * Express模版。基于模版文件解析内容<br/>
 *
 * @author j.x
 * @date 2021/12/25 16:40
 * @see PlaceholderExpressionTemplate
 * @since 1.0
 */
public interface ExpressionTemplate {

    /**
     * Express模板文件的后缀
     */
    String FILE_SUFFIX = ".template";

    /**
     * 解析模板，当发生错误时将保持原样
     *
     * @param template 模板
     * @param target   填充于模版的目标对象，要求是一个POJO对象
     * @return 解析完成的字符串
     * @throws NullPointerException template target为空时抛出
     */
    String parseTemplate(String template, Object target);

    /**
     * 解析模板
     *
     * @param template 模板
     * @param k1       模板变量k
     * @param v1       模板变量v
     * @return 解析完成的字符串
     */
    default String parseTemplate(String template, String k1, Object v1) {
        return parseTemplate(template, Tuples.of(k1, v1));
    }

    /**
     * 解析模板
     *
     * @param template 模板
     * @param k1       模板变量k1
     * @param v1       模板变量v1
     * @param k2       模板变量k2
     * @param v2       模板变量v2
     * @return 解析完成的字符串
     */
    default String parseTemplate(String template, String k1, Object v1, String k2, Object v2) {
        return parseTemplate(template, Tuples.of(k1, v1, k2, v2));
    }

    /**
     * 解析模板
     *
     * @param template 模板
     * @param k1       模板变量k1
     * @param v1       模板变量v1
     * @param k2       模板变量k2
     * @param v2       模板变量v2
     * @param k3       模板变量k3
     * @param v3       模板变量v2
     * @return 解析完成的字符串
     */
    default String parseTemplate(String template, String k1, Object v1, String k2, Object v2, String k3, Object v3) {
        return parseTemplate(template, Tuples.of(k1, v1, k2, v2, k3, v3));
    }

    /**
     * 解析模板
     *
     * @param template 模板
     * @param kv       模板变量
     * @return 解析完成的字符串
     */
    default String parseTemplate(String template, Tuple2<String, Object> kv) {
        return parseTemplate(template, Collections.singletonMap(kv.getT1(), kv.getT2()));
    }

    /**
     * 解析模板
     *
     * @param template 模板
     * @param kv       模板变量
     * @return 解析完成的字符串
     */
    default String parseTemplate(String template, Tuple4<String, Object, String, Object> kv) {
        Map<String, Object> variables = Maps.newHashMap();
        variables.put(kv.getT1(), kv.getT2());
        variables.put(kv.getT3(), kv.getT4());
        return parseTemplate(template, variables);
    }

    /**
     * 解析模板
     *
     * @param template 模板
     * @param kv       模板变量
     * @return 解析完成的字符串
     */
    default String parseTemplate(String template, Tuple6<String, Object, String, Object, String, Object> kv) {
        Map<String, Object> variables = Maps.newHashMap();
        variables.put(kv.getT1(), kv.getT2());
        variables.put(kv.getT3(), kv.getT4());
        variables.put(kv.getT5(), kv.getT6());
        return parseTemplate(template, variables);
    }

    /**
     * 解析文件模板
     *
     * @param file   文件路径
     * @param target 填充于模版的目标对象，要求是一个POJO对象
     * @return 解析完成的字符串
     * @throws IOException 文件找不到或者文件不是.template后缀抛出
     */
    default String parseFileTemplate(String file, Object target) throws IOException {
        if (!file.contains(FILE_SUFFIX)) {
            throw new FilerException("file suffix error, expect .template");
        }
        URL url = Thread.currentThread().getContextClassLoader().getResource(file);
        if (url != null) {
            UrlResource resource = new UrlResource(url);
            String template = IoUtils.readToString(resource.getInputStream());
            return parseTemplate(template, target);
        }
        throw new FileNotFoundException(String.format("%s file not found", file));
    }

    /**
     * 创建默认模板解析实例
     *
     * @return ExpressionTemplate实例
     * @see PlaceholderExpressionTemplate
     * @see Tokenizer
     * @see Tokenizer#HASH_BRACE
     * @see #createTemplate(Tokenizer)
     */
    static ExpressionTemplate defaultTemplate() {
        return createTemplate(Tokenizer.HASH_BRACE);
    }

    /**
     * 根据指定的{@link Tokenizer}创建{@link ExpressionTemplate}实例
     *
     * @param tokenizer Tokenizer实例对象
     * @return ExpressionTemplate实例
     * @see PlaceholderExpressionTemplate
     * @see Tokenizer
     */
    static ExpressionTemplate createTemplate(Tokenizer tokenizer) {
        return new PlaceholderExpressionTemplate(tokenizer);
    }

    /**
     * 根据指定的{@link Tokenizer}创建{@link ExpressionTemplate}实例
     *
     * @param tokenizer Tokenizer实例对象
     * @param langsym   语言类型值。如 String 32 = "32"
     * @return ExpressionTemplate实例
     * @see PlaceholderExpressionTemplate
     * @see Tokenizer
     */
    static ExpressionTemplate createTemplate(Tokenizer tokenizer, boolean langsym) {
        return new PlaceholderExpressionTemplate(tokenizer, langsym);
    }

    /**
     * 根据指定的{@link Tokenizer}创建{@link TokenParser}实例
     *
     * @param tokenizer tokenizer
     * @return TokenParser
     */
    static TokenParser createParse(Tokenizer tokenizer) {
        return new GenericTokenParser(tokenizer);
    }

    /**
     * @see #parseTemplate(String, Object)
     */
    static String parse(String template, Object target) {
        return defaultTemplate().parseTemplate(template, target);
    }

    /**
     * @see #parseTemplate(String, String, Object)
     */
    static String parse(String template, String k1, Object v1) {
        return defaultTemplate().parseTemplate(template, k1, v1);
    }

    /**
     * @see #parseTemplate(String, String, Object, String, Object)
     */
    static String parse(String template, String k1, Object v1, String k2, Object v2) {
        return defaultTemplate().parseTemplate(template, k1, v1, k2, v2);
    }

    /**
     * @see #parseTemplate(String, String, Object, String, Object, String, Object)
     */
    static String parse(String template, String k1, Object v1, String k2, Object v2, String k3, Object v3) {
        return defaultTemplate().parseTemplate(template, k1, v2, k2, v2, k3, v3);
    }

    /**
     * @see #parseTemplate(String, Tuple2)
     */
    static String parse(String template, Tuple2<String, Object> kv) {
        return defaultTemplate().parseTemplate(template, kv);
    }

    /**
     * @see #parseTemplate(String, Tuple4)
     */
    static String parse(String template, Tuple4<String, Object, String, Object> kv) {
        return defaultTemplate().parseTemplate(template, kv);
    }

    /**
     * @see #parseTemplate(String, Tuple6)
     */
    static String parse(String template, Tuple6<String, Object, String, Object, String, Object> kv) {
        return defaultTemplate().parseTemplate(template, kv);
    }
}
