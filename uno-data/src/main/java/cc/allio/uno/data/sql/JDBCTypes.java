package cc.allio.uno.data.sql;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.util.type.Types;

/**
 * 处理JDBC相关类型
 *
 * @author jiangwei
 * @date 2023/1/5 19:59
 * @since 1.1.4
 */
public class JDBCTypes extends Types {

    /**
     * 向给定参数的字符串添加'
     *
     * @param value 未添加
     * @return 添加后的字符串
     */
    public static String addQuote(String value) {
        return StringPool.SINGLE_QUOTE + value + StringPool.SINGLE_QUOTE;
    }
}
