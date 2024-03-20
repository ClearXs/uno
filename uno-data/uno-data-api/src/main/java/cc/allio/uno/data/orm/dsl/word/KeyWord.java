package cc.allio.uno.data.orm.dsl.word;

import lombok.AllArgsConstructor;

/**
 * SQL 关键值
 *
 * @author j.x
 * @date 2023/1/12 16:42
 * @since 1.1.4
 */
@AllArgsConstructor
public class KeyWord {

    private final String name;

    /**
     * 获取不区分大小写关键字
     *
     * @return
     */
    public String caseInsensitive() {
        return name;
    }

    /**
     * 根据配置，确定关键字使用大写 or 小写
     *
     * @return 大写 or 小写
     */
    public String get() {
        return name;
    }

    /**
     * 获取小写关键字
     *
     * @return 小写关键字
     */
    public String getLower() {
        return name.toLowerCase();
    }

    /**
     * 获取大写关键字
     *
     * @return 大写关键字
     */
    public String getUpper() {
        return name.toUpperCase();
    }
}
