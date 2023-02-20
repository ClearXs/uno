package cc.allio.uno.data.orm.dialect.type;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.data.sql.word.KeyWord;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Objects;

/**
 * 默认实现
 *
 * @author jiangwei
 * @date 2023/1/13 14:48
 * @since 1.1.4
 */
public class JdbcTypeImpl implements JdbcType {

    private final KeyWord name;
    private final String vendor;
    private final int jdbcCode;
    private final boolean isDecimal;
    private final String signature;
    private final boolean includeParenthesis;
    private final Long maxBytes;

    private final List<String> parameters;

    public JdbcTypeImpl(String name, String vendor, int jdbcCode, boolean isDecimal, String signature, boolean includeParenthesis, Long maxBytes, String[] parameters) {
        this.name = new KeyWord(name);
        this.vendor = vendor;
        this.jdbcCode = jdbcCode;
        this.isDecimal = isDecimal;
        this.signature = signature;
        this.includeParenthesis = includeParenthesis;
        this.maxBytes = maxBytes;
        this.parameters = Lists.newArrayList(parameters);
    }

    @Override
    public String getName() {
        StringBuilder type = new StringBuilder(name.get());
        if (includeParenthesis) {
            // append ($1,$1)
            type.append(StringPool.LEFT_BRACKET);
            type.append(String.join(StringPool.COMMA, parameters));
            type.append(StringPool.RIGHT_BRACKET);
        }
        return type.toString();
    }

    @Override
    public String getVendor() {
        return vendor;
    }

    @Override
    public Integer getVendorTypeNumber() {
        return vendor.hashCode();
    }

    @Override
    public int getJdbcCode() {
        return jdbcCode;
    }

    @Override
    public boolean includeParenthesis() {
        return includeParenthesis;
    }

    @Override
    public boolean isDecimal() {
        return isDecimal;
    }

    @Override
    public Long getMaxBytes() {
        return maxBytes;
    }

    @Override
    public String getSignature() {
        return signature;
    }

    @Override
    public JdbcType setParameterPattern(String pattern) {
        parameters.add(pattern);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JdbcTypeImpl jdbcType = (JdbcTypeImpl) o;
        return jdbcCode == jdbcType.jdbcCode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(jdbcCode);
    }
}
