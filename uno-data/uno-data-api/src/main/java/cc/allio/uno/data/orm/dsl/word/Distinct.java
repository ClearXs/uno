package cc.allio.uno.data.orm.dsl.word;

import cc.allio.uno.data.orm.dsl.DSLName;

/**
 * distinct
 *
 * @author j.x
 * @date 2023/1/12 17:18
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
