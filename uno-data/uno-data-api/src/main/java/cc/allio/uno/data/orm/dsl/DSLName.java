package cc.allio.uno.data.orm.dsl;

import cc.allio.uno.core.env.Envs;
import cc.allio.uno.core.util.StringUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.regex.Pattern;

/**
 * dsl name 如 xxxx name、column name...根据指定格式化转换对应的name
 *
 * @author jiangwei
 * @date 2023/4/17 16:16
 * @since 1.1.4
 */
@Getter
@EqualsAndHashCode(of = "name")
public class DSLName implements Comparable<DSLName> {

    // 当前存入的dsl name
    private String name;

    private NameFeature feature;

    private static final String HUMP_REGULAR = "^[a-z]+(?:[A-Z][a-z]*)*$";
    private static final Pattern HUMP_PATTERN = Pattern.compile(HUMP_REGULAR);

    private static final String UNDERLINE_REGULAR = "^[a-z]+(?:_[a-z]+)*$";
    private static final Pattern UNDERLINE_PATTERN = Pattern.compile(UNDERLINE_REGULAR);

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

    public static final NameFeature UNDERLINE_FEATURE =
            ori -> {
                if (UNDERLINE_PATTERN.matcher(ori).matches()) {
                    return ori;
                }
                if (HUMP_PATTERN.matcher(ori).matches()) {
                    return StringUtils.camelToUnderline(ori);
                }
                return ori;
            };

    public static final NameFeature HUMP_FEATURE =
            ori -> {
                if (HUMP_PATTERN.matcher(ori).matches()) {
                    return ori;
                }
                if (UNDERLINE_PATTERN.matcher(ori).matches()) {
                    return StringUtils.underlineToCamel(ori);
                }
                return ori;
            };

    public static final NameFeature PLAIN_FEATURE = ori -> ori;
    public static final NameFeature LOWER_CASE_FEATURE = String::toLowerCase;
    public static final NameFeature UPPER_CASE_FEATURE = String::toUpperCase;

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
    public static DSLName of(String name) {
        return of(name, getNameFeature());
    }

    /**
     * 创建SQLName 实例
     *
     * @param name    name
     * @param feature feature
     * @return SQLName
     */
    public static DSLName of(String name, NameFeature feature) {
        DSLName sqlName = new DSLName();
        sqlName.name = name;
        sqlName.feature = feature;
        return sqlName;
    }

    /**
     * 创建SQLName 实例
     *
     * @param sqlName sqlName
     * @param feature feature
     * @return SQLName
     */
    public static DSLName of(DSLName sqlName, NameFeature... feature) {
        DSLName newName = new DSLName();
        newName.name = sqlName.name;
        newName.setFeature(feature);
        return newName;
    }

    private void setFeature(NameFeature... features) {
        this.feature = new AggregateNameFeature(features);
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

    @Override
    public int compareTo(DSLName o) {
        return this.name.compareTo(o.name);
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

    static class AggregateNameFeature implements NameFeature {

        private final NameFeature[] features;

        AggregateNameFeature(NameFeature... features) {
            this.features = features;
        }

        @Override
        public String format(String o) {
            for (NameFeature feature : features) {
                o = feature.format(o);
            }
            return o;
        }
    }
}
