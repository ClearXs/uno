package cc.allio.uno.data.orm.dsl;

/**
 * SQL join
 *
 * @author j.x
 * @since 1.1.4
 */
public enum JoinType {

    COMMA(","),
    JOIN("JOIN"),
    INNER_JOIN("INNER JOIN"),
    CROSS_JOIN("CROSS JOIN"),
    NATURAL_JOIN("NATURAL JOIN"),
    NATURAL_CROSS_JOIN("NATURAL CROSS JOIN"),
    NATURAL_LEFT_JOIN("NATURAL LEFT JOIN"),
    NATURAL_RIGHT_JOIN("NATURAL RIGHT JOIN"),
    NATURAL_INNER_JOIN("NATURAL INNER JOIN"),
    LEFT_OUTER_JOIN("LEFT JOIN"),
    LEFT_SEMI_JOIN("LEFT SEMI JOIN"),
    LEFT_ANTI_JOIN("LEFT ANTI JOIN"),
    RIGHT_OUTER_JOIN("RIGHT JOIN"),
    FULL_OUTER_JOIN("FULL JOIN"),
    STRAIGHT_JOIN("STRAIGHT_JOIN"),
    OUTER_APPLY("OUTER APPLY"),
    CROSS_APPLY("CROSS APPLY");

    public final String name;
    public final String nameLCase;

    JoinType(String name) {
        this.name = name;
        this.nameLCase = name.toLowerCase();
    }
}
