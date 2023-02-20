package cc.allio.uno.component.http.openapi;

/**
 * 解析器接口，分为两个部分
 * 1.主解析器，继承{@link  AbstractPrimaryParser}的类
 * 2.次解析器
 * 主解析器关联者次解析器，在主的解析器的生命周期内，将会调用次解析器的生命周期。
 *
 * @author jw
 * @date 2021/12/5 9:55
 */
public interface Parser<T> {

    /**
     * 解析器初始化<br/>
     * 1.向Mapper中注册Module
     * 2.存储当前子解析器的对象
     *
     * @param context 解析器上下文对象
     */
    void init(ParserContext context);


    /**
     * 解析前动作
     *
     * @param context 解析上下文对象
     */
    void preParse(ParserContext context);

    /**
     * 解析传入的字符串，得到需要的对象
     *
     * @param unresolved 等待解析的字符串
     * @param context    解析器上下文对象
     * @return 解析完成的实例对象
     */
    T parse(String unresolved, ParserContext context);

}
