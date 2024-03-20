package cc.allio.uno.data.orm.dsl;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * SQL相关的操作符
 *
 * @author j.x
 * @date 2023/4/13 13:10
 * @since 1.1.4
 */
@Getter
@AllArgsConstructor
public enum TokenOperator {
    UNION("UNION"),
    COLLATE("COLLATE"),
    BITWISE_XOR("^"),
    BITWISE_XOR_EQ("^="),
    MULTIPLY("*"),
    DIVIDE("/"),
    DIV("DIV"), // mysql integer division
    MODULUS("%"),
    MOD("MOD"),
    ADD("+"),
    SUBTRACT("-"),
    SUB_GT("->"),
    SUB_GT_GT("->>"),
    POUND_GT("#>"),
    POUND_GT_GT("#>>"),
    QUES_QUES("??"),
    QUES_BAR("?|"),
    QUES_AMP("?&"),
    LEFT_SHIFT("<<"),
    RIGHT_SHIFT(">>"),
    BITWISE_AND("&"),
    BITWISE_OR("|"),
    GREATER_THAN(">"),
    GREATER_THAN_OR_EQUAL(">="),
    IS("IS"),
    LESS_THAN("<"),
    LESS_THAN_OR_EQUAL("<="),
    LESS_THAN_OR_EQUAL_OR_GREATER_THAN("<=>"),
    LESS_THAN_OR_GREATER("<>"),
    IS_DISTINCT_FROM("IS DISTINCT FROM"),
    IS_NOT_DISTINCT_FROM("IS NOT DISTINCT FROM"),
    LIKE("LIKE"),
    SOUDS_LIKE("SOUNDS LIKE"),
    NOT_LIKE("NOT LIKE"),
    I_LIKE("ILIKE"),
    NOT_I_LIKE("NOT ILIKE"),
    AT_AT("@@"), // postgresql textsearch
    SIMILAR_TO("SIMILAR TO"),
    POSIX_REGULAR_MATCH("~"),
    POSIX_REGULAR_MATCH_INSENSITIVE("~*"),
    POSIX_REGULAR_NOT_MATCH("!~"),
    POSIX_REGULAR_NOT_MATCH_POSIX_REGULAR_MATCH_INSENSITIVE("!~*"),
    ARRAY_CONTAINS("@>"),
    ARRAY_CONTAINED_BY("<@"),
    SAME_AS("~="),
    JSON_CONTAINS("?"),
    R_LIKE("RLIKE"),
    NOT_R_LIKE("NOT RLIKE"),
    NOT_EQUAL("!="),
    NOT_LESS_THAN("!<"),
    NOT_GREATER_THAN("!>"),
    IS_NOT("IS NOT"),
    ESCAPE("ESCAPE"),
    REG_EXP("REGEXP"),
    NOT_REG_EXP("NOT REGEXP"),
    EQUALITY("="),
    BITWISE_NOT("!"),
    CONCAT("||"),
    BOOLEAN_AND("AND"),
    BOOLEAN_XOR("XOR"),
    BOOLEAN_OR("OR"),
    ASSIGNMENT(":="),
    PG_And("&&"),
    PG_ST_DISTANCE("<->");
    private final String name;
}
