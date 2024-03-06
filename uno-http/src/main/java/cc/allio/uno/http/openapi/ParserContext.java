package cc.allio.uno.http.openapi;

import cc.allio.uno.core.serializer.Serializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * 存储着解析使用的工具
 *
 * @author jw
 * @date 2021/12/5 10:12
 */
public interface ParserContext {

    /**
     * jackson解析json的对象。<br>
     * <b>需要注意的点是，当使用这个Mapper反序列化时，如果它里面存在自定义反序列化器解析的类型，那么这个类型就不能在使用它解析。</b><br/>
     *
     * @return ObjectMapper实例
     */
    ObjectMapper mapper();

    /**
     * 具体序列化的module对象，mapper中无法获取
     */
    SimpleModule module();

    /**
     * 解析器执行者
     *
     * @return 实例对象
     */
    ParserExecution execution();

    /**
     * 序列化器
     *
     * @return 序列化器实例
     */
    Serializer serializer();
}
