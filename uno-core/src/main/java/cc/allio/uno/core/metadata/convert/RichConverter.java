package cc.allio.uno.core.metadata.convert;

import cc.allio.uno.core.metadata.Metadata;

/**
 * rich Converter操作.
 *
 * @author j.x
 * @since 1.1.0
 */
public interface RichConverter<T extends Metadata> extends Converter<T> {

    /**
     * 获取转换类型
     *
     * @return Class类型对象
     */
    Class<? extends T> getConvertType();

}
