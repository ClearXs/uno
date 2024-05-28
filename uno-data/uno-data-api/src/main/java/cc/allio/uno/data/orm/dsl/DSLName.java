package cc.allio.uno.data.orm.dsl;

import cc.allio.uno.core.api.EqualsTo;
import cc.allio.uno.core.env.Envs;
import cc.allio.uno.core.util.StringUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * dsl name 如 xxxx name、column name...根据指定格式化转换对应的name
 *
 * @author j.x
 * @date 2023/4/17 16:16
 * @since 1.1.4
 */
@Getter
@Setter
@EqualsAndHashCode(of = "name")
public class DSLName implements Comparable<DSLName>, EqualsTo<DSLName>, Meta<DSLName> {

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
    private static final String NAME_FORMAT_KEY = "allio.uno.data.orm.dsl.name.format";
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

    public static final NameFeature UNDERLINE_FEATURE = new UnderlineFeature();
    public static final NameFeature HUMP_FEATURE = new HumpFeature();
    public static final NameFeature PLAIN_FEATURE = new PlainFeature();
    public static final NameFeature LOWER_CASE_FEATURE = new LowerCaseFeature();
    public static final NameFeature UPPER_CASE_FEATURE = new UpperCaseFeature();

    public DSLName() {

    }

    public DSLName(String name) {
        this(name, getNameFeature());
    }

    public DSLName(String name, NameFeature nameFeature) {
        this.name = name;
        this.feature = nameFeature;
    }

    public DSLName(DSLName dslName, NameFeature... nameFeatures) {
        this.name = dslName.format();
        this.feature = NameFeature.aggregate(nameFeatures);
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
     * format dsl name to underline
     *
     * @return underline string
     * @see #UNDERLINE_FEATURE
     */
    public String formatUnderlie() {
        return format(UNDERLINE_FEATURE);
    }

    /**
     * format dsl name to hump
     *
     * @return hump string
     * @see #HUMP_FEATURE
     */
    public String formatHump() {
        return format(HUMP_FEATURE);
    }

    /**
     * format dsl name to lower case
     *
     * @return lower case string
     * @see #LOWER_CASE_FEATURE
     */
    public String formatLowerCase() {
        return format(LOWER_CASE_FEATURE);
    }

    /**
     * format dsl name to upper case
     *
     * @return upper case string
     * @see #UPPER_CASE_FEATURE
     */
    public String formatUpperCase() {
        return format(UPPER_CASE_FEATURE);
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

    @Override
    public boolean equalsTo(DSLName other) {
        // 如果名称不匹配，则尝试使用format
        if (!this.name.equals(other.name)) {
            return format().equals(other.name);
        }
        return true;
    }

    /**
     * 创建{@link DSLName}实例
     *
     * @param name name
     * @return {@link DSLName}
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

    /**
     * 下划线转驼峰
     *
     * @param underline underline
     * @return hump string
     */
    public static String toHump(String underline) {
        return DSLName.of(underline, HUMP_FEATURE).format();
    }

    /**
     * 驼峰转下划线
     *
     * @param hump hump
     * @return underline string
     */
    public static String toUnderline(String hump) {
        return DSLName.of(hump, UNDERLINE_FEATURE).format();
    }

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

        /**
         * 集合于{@link NameFeature}
         *
         * @param nameFeatures nameFeatures
         * @return NameFeature
         */
        static NameFeature aggregate(NameFeature... nameFeatures) {
            return new AggregateNameFeature(nameFeatures);
        }
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

    static class UnderlineFeature implements NameFeature {

        @Override
        public String format(String ori) {
            if (UNDERLINE_PATTERN.matcher(ori).matches()) {
                return ori;
            }
            if (HUMP_PATTERN.matcher(ori).matches()) {
                return StringUtils.camelToUnderline(ori);
            }
            return ori;
        }
    }

    static class HumpFeature implements NameFeature {

        @Override
        public String format(String ori) {
            if (HUMP_PATTERN.matcher(ori).matches()) {
                return ori;
            }
            if (UNDERLINE_PATTERN.matcher(ori).matches()) {
                return StringUtils.underlineToCamel(ori);
            }
            return ori;
        }
    }

    static class LowerCaseFeature implements NameFeature {

        @Override
        public String format(String o) {
            return o.toLowerCase();
        }
    }

    static class PlainFeature implements NameFeature {

        @Override
        public String format(String o) {
            return o;
        }
    }

    static class UpperCaseFeature implements NameFeature {

        @Override
        public String format(String o) {
            return o.toUpperCase();
        }
    }
}
