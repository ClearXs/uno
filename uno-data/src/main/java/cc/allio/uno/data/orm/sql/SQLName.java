package cc.allio.uno.data.orm.sql;

import cc.allio.uno.core.env.Envs;
import cc.allio.uno.core.util.StringUtils;
import lombok.Getter;

/**
 * sql name 如 table name、column name...根据指定格式化转换对应的name
 *
 * @author jiangwei
 * @date 2023/4/17 16:16
 * @since 1.1.4
 */
@Getter
public class SQLName {

    // 当前存入的sql name
    private String name;

    private NameFeature feature;

    /**
     * 名称格式化key
     */
    private static final String NAME_FORMAT_KEY = "allio.uno.data.orm.sql.name.format";
    // 名称以下划线
    private static final String NAME_FORMAT_UNDERLINE = "underline";
    // 名称以驼峰
    private static final String NAME_FORM_HUMP = "hump";
    // 名称以原样
    private static final String NAME_FORM_PLAIN = "plain";
    // 名称以小写
    private static final String NAME_FORM_LOWER_CASE = "lower-case";
    // 名称以大写
    private static final String NAME_FORM_UPPER_CASE = "upper-case";

    public static final NameFeature UNDERLINE_FEATURE = new UnderlineNameFeature();
    public static final NameFeature HUMP_FEATURE = new HumpNameFeature();
    public static final NameFeature PLAIN_FEATURE = new PlainNameFeature();
    public static final NameFeature LOWER_CASE_FEATURE = new LowerCaseFeature();
    public static final NameFeature UPPER_CASE_FEATURE = new UpperCaseFeature();

    /**
     * 获取name feature
     *
     * @return NameFeature or default {@link #PLAIN_FEATURE}
     */
    public static NameFeature getNameFeature() {
        String nameFormatKey = Envs.getProperty(NAME_FORMAT_KEY);
        if (NAME_FORMAT_UNDERLINE.equals(nameFormatKey)) {
            return UNDERLINE_FEATURE;
        } else if (NAME_FORM_HUMP.equals(nameFormatKey)) {
            return HUMP_FEATURE;
        } else if (NAME_FORM_PLAIN.equals(nameFormatKey)) {
            return PLAIN_FEATURE;
        } else if (NAME_FORM_LOWER_CASE.equals(nameFormatKey)) {
            return LOWER_CASE_FEATURE;
        } else if (NAME_FORM_UPPER_CASE.equals(nameFormatKey)) {
            return UPPER_CASE_FEATURE;
        }
        return UNDERLINE_FEATURE;
    }

    /**
     * 创建SQLName实例
     *
     * @param name name
     * @return SQLName
     */
    public static SQLName of(String name) {
        return of(name, getNameFeature());
    }

    /**
     * 创建SQLName 实例
     *
     * @param name    name
     * @param feature feature
     * @return SQLName
     */
    public static SQLName of(String name, NameFeature feature) {
        SQLName sqlName = new SQLName();
        sqlName.name = name;
        sqlName.feature = feature;
        return sqlName;
    }

    /**
     * 把当前存入的sql name进行格式化
     *
     * @return 格式化后的name
     */
    public String format() {
        return getFeature().format(getName());
    }

    /**
     * 根据指定的名称特性
     *
     * @param feature the feature
     * @return format the name
     */
    public String format(NameFeature feature) {
        if (feature == null) {
            throw new IllegalArgumentException("NameFeature is null");
        }
        return feature.format(getName());
    }

    /**
     * 名称特性
     */
    public interface NameFeature {

        /**
         * 格式化
         *
         * @param o 原始数据
         * @return 格式化后的名称
         */
        String format(String o);
    }

    /**
     * 不进行转换
     */
    public static class PlainNameFeature implements NameFeature {

        @Override
        public String format(String o) {
            return o;
        }
    }

    /**
     * 小写
     */
    public static class LowerCaseFeature implements NameFeature {

        @Override
        public String format(String o) {
            return o.toLowerCase();
        }
    }

    /**
     * 大写
     */
    public static class UpperCaseFeature implements NameFeature {

        @Override
        public String format(String o) {
            return o.toUpperCase();
        }
    }

    /**
     * 下划线
     */
    public static class UnderlineNameFeature implements NameFeature {

        @Override
        public String format(String o) {
            return StringUtils.camelToUnderline(o);
        }
    }

    /**
     * 驼峰
     */
    public static class HumpNameFeature implements NameFeature {

        @Override
        public String format(String o) {
            return StringUtils.underlineToCamel(o);
        }
    }
}
