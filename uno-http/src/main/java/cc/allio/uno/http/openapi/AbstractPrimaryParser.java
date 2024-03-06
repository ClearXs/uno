package cc.allio.uno.http.openapi;

import cc.allio.uno.core.serializer.SerializerHolder;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.util.List;
import java.util.Optional;

/**
 * 抽象解析器，泛化一些操作，实现解耦，复用。<br/>
 * 实现抽象类的字类必须提供SimpleModule的支持，也就是说在具体解析的过程中需要自定义序列化器与反序列化器。<br/>
 *
 * @author jw
 * @date 2021/12/5 12:34
 */
public abstract class AbstractPrimaryParser<T> implements Parser<T> {

    @Override
    public void init(ParserContext context) {
        SimpleModule module = context.module();
        if (module == null) {
            module = Optional
                    .ofNullable(newMapperModule())
                    .orElseThrow(() -> new NullPointerException("support Module is null"));
            ((DefaultParserContext) context).setModule(module);
        }
        // 注册子解析器，子解析器将会向Module中添加序列化器（Jackson中）
        supportSubParser().forEach(sub -> context.execution().register(sub, context));
        // 把module放入ObjectMapper中，Mapper将会根据Module中存放的序列化器把它放入factory中，当之后遇到需要反序列化的类型从中拿取。
        // 所以才会把module延迟到这一步注册。
        context.mapper().registerModule(module);
        // 设置序列化器
        ((DefaultParserContext) context).setSerializer(SerializerHolder.holder().get());
    }

    @Override
    public void preParse(ParserContext context) {
        doPreParser(context);
    }

    @Override
    public T parse(String unresolved, ParserContext context) {
        return doParse(unresolved, context);
    }

    /**
     * 子类具体实现解析的操作
     *
     * @param unresolved 未解析的字符串
     * @param context    解析器的上下文
     * @return 解析后实体结果
     */
    protected abstract T doParse(String unresolved, ParserContext context);

    /**
     * 子类具体实现预处理的操作
     *
     * @param context 解析器上下文
     */
    protected abstract void doPreParser(ParserContext context);


    /**
     * 子类提供MapperModule <br/>
     * <b>require：字类提供的Module不能为空并且它的名称为当前类的全限定名称</b>
     *
     * @return 一个SimpleModule对象
     */
    protected abstract SimpleModule newMapperModule();

    /**
     * 由子类提供子解析器
     *
     * @return 子解析器List对象
     */
    protected abstract List<Parser<?>> supportSubParser();

}
