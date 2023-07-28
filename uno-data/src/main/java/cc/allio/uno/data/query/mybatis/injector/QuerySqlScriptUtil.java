package cc.allio.uno.data.query.mybatis.injector;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;

/**
 * 查询脚本工具
 *
 * @author jiangwei
 * @date 2022/9/30 16:38
 * @since 1.1.0
 */
public abstract class QuerySqlScriptUtil extends SqlScriptUtils {

    public static String orderSql(String sql) {
        return "ORDER BY" + NEWLINE + sql;
    }

    public static String groupSql(String sql) {
        return "GROUP BY" + NEWLINE + sql;
    }

    public static String convertForeach(final String sqlScript, final String collection, final String index,
                                        final String item, final String open, final String close, final String separator) {
        StringBuilder sb = new StringBuilder("<foreach");
        if (StringUtils.isNotBlank(collection)) {
            sb.append(" collection=\"").append(collection).append(QUOTE);
        }
        if (StringUtils.isNotBlank(index)) {
            sb.append(" index=\"").append(index).append(QUOTE);
        }
        if (StringUtils.isNotBlank(item)) {
            sb.append(" item=\"").append(item).append(QUOTE);
        }
        if (StringUtils.isNotBlank(separator)) {
            sb.append(" separator=\"").append(separator).append(QUOTE);
        }
        if (StringUtils.isNotBlank(open)) {
            sb.append(" open=\"").append(open).append(QUOTE);
        }
        if (StringUtils.isNotBlank(close)) {
            sb.append(" close=\"").append(close).append(QUOTE);
        }
        return sb.append(RIGHT_CHEV).append(NEWLINE).append(sqlScript).append(NEWLINE).append("</foreach>").toString();
    }
}
