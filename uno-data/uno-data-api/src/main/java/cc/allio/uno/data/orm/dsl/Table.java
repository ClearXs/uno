package cc.allio.uno.data.orm.dsl;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 表
 *
 * @author jiangwei
 * @date 2023/1/9 12:16
 * @since 1.1.4
 */
@Data
@Accessors(chain = true)
public class Table {

    /**
     * 表名
     */
    private DSLName name;

    /**
     * 别名
     */
    private String alias;
    private String schema = "PUBLIC";
    private String catalog;
    private String type;
    private String comment;

    /**
     * 根据表名创建{@link Table}实例
     *
     * @param name the name
     */
    public static Table of(String name) {
        Table table = new Table();
        table.setName(DSLName.of(name));
        return table;
    }

    public static Table of(String name, String alias) {
        Table table = new Table();
        table.setName(DSLName.of(name));
        table.setAlias(alias);
        return table;
    }

    public static Table of(DSLName name) {
        return of(name, null);
    }

    public static Table of(DSLName name, String alias) {
        Table table = new Table();
        table.setName(name);
        table.setAlias(alias);
        return table;
    }

}
