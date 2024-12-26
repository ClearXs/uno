package cc.allio.uno.core.type;

/**
 * 模糊匹配类型
 *
 * @author j.x
 * @since 1.1.4
 */
public class RegexType implements Type {

    private final String regex;

    public RegexType(String regex) {
        this.regex = regex;
    }

    @Override
    public String getCode() {
        return regex;
    }
}
