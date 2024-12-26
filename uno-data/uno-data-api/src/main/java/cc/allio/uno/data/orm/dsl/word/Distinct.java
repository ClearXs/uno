package cc.allio.uno.data.orm.dsl.word;

import cc.allio.uno.data.orm.dsl.DSLName;

/**
 * distinct
 *
 * @author j.x
 * @since 1.1.4
 */
public class Distinct extends KeyWord {

    public Distinct() {
        super("distinct");
    }

    public Distinct(DSLName name) {
        super(name.format());
    }
}
