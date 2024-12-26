package cc.allio.uno.data.orm.dsl.type;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.util.ObjectUtils;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.core.util.template.Tokenizer;

import java.io.Serializable;
import java.sql.SQLType;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * jdbc type定义。与{@link JavaType}成映射关系。
 *
 * @author j.x
 * @see java.sql.SQLType
 * @see TypeRegistry
 * @since 1.1.4
 */
public interface JdbcType extends SQLType, Serializable {

    /**
     * 获取当前jdbc type的名称
     *
     * @return bit varchar...
     */
    @Override
    String getName();

    /**
     * 获取jdbc对应的code
     *
     * @return jdbc code
     * @see java.sql.Types
     */
    int getJdbcCode();

    /**
     * 是否包含括号
     *
     * @return true 包含 false 不包含
     */
    boolean includeParenthesis();

    /**
     * 判断当前类型是否是小数
     *
     * @return true 小数 false 不是小数
     */
    boolean isDecimal();

    /**
     * 给定数据类型支持的最大的字节大小
     *
     * @return 支持
     */
    Long getMaxBytes();

    /**
     * 获取当前jdbc签名信息
     *
     * @return 类型 签名
     */
    String getSignature();

    /**
     * 设置类型参数
     *
     * @param pattern 符号
     * @return JdbcType
     */
    JdbcType setParameterPattern(String pattern);

    /**
     * 获取{@link JdbcType}构造器
     *
     * @return JdbcTypeBuilder 实例
     */
    static JdbcTypeBuilder builder() {
        return new JdbcTypeBuilder();
    }

    /**
     * {@link JdbcType}构建器
     *
     * @author j.x
     * @since 1.1.4
     */
    class JdbcTypeBuilder {
        private String name;
        private String vendor;
        private int jdbcCode;
        private boolean isDecimal;
        private Long maxBytes;
        private String signature;
        private boolean includeParenthesis;

        /**
         * 设置 type name
         *
         * @param name name
         * @return JdbcTypeBuilder
         */
        public JdbcTypeBuilder setName(String name) {
            this.name = name;
            return this;
        }

        /**
         * 设置vendor
         *
         * @param vendor vendor
         * @return JdbcTypeBuilder
         */
        public JdbcTypeBuilder setVendor(String vendor) {
            this.vendor = vendor;
            return this;
        }

        /**
         * 设置jdbcCode
         *
         * @param jdbcCode jdbcCode
         * @return JdbcTypeBuilder
         */
        public JdbcTypeBuilder setJdbcCode(int jdbcCode) {
            this.jdbcCode = jdbcCode;
            return this;
        }

        /**
         * 是否Decimal
         *
         * @param isDecimal 是否Decimal
         * @return JdbcTypeBuilder
         */
        public JdbcTypeBuilder isDecimal(boolean isDecimal) {
            this.isDecimal = isDecimal;
            return this;
        }

        /**
         * 设置类型签名。<b>type([$p], [$d]...)</b>
         *
         * @param signature 签名
         * @return JdbcTypeBuilder
         */
        public JdbcTypeBuilder setSignature(String signature) {
            this.signature = signature;
            return this;
        }

        /**
         * 是否包含括号
         *
         * @param includeParenthesis 是否包含括号
         * @return JdbcTypeBuilder
         */
        public JdbcTypeBuilder isIncludeParenthesis(boolean includeParenthesis) {
            this.includeParenthesis = includeParenthesis;
            return this;
        }

        /**
         * 设置当前类型最大字节数
         *
         * @param maxBytes 最大字节数
         * @return JdbcTypeBuilder
         */
        public JdbcTypeBuilder setMaxBytes(Long maxBytes) {
            this.maxBytes = maxBytes;
            return this;
        }

        /**
         * 构建JdbcTypeImpl
         *
         * @return JdbcTypeImpl
         */
        public JdbcTypeImpl build() {
            String[] parameters = new String[]{};
            if (StringUtils.isNotBlank(signature)) {
                // $p, $d
                parameters = Arrays.stream(StringUtils.split(signature, Tokenizer.DOUBLE_BRACKET))
                        .flatMap(token -> Stream.of(token.split(StringPool.COMMA)))
                        .toArray(String[]::new);
                // 判断是否包含()
                if (!ObjectUtils.isEmpty(parameters)) {
                    includeParenthesis = true;
                    // name
                    name = signature.split(StringPool.BACK_SLASH + StringPool.LEFT_BRACKET)[0];
                }
            }

            return new JdbcTypeImpl(name, vendor, jdbcCode, isDecimal, signature, includeParenthesis, maxBytes, parameters);
        }
    }

}
