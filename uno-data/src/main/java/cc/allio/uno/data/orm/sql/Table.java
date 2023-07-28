package cc.allio.uno.data.orm.sql;

import lombok.Data;

/**
 * 表
 *
 * @author jiangwei
 * @date 2023/1/9 12:16
 * @since 1.1.4
 */
@Data
public class Table {

    /**
     * 表名
     */
    private SQLName name;

    /**
     * 别名
     */
    private String alias;

    private String catalog;

    /**
     * 根据表名创建{@link Table}实例
     *
     * @param name the name
     */
    public static Table of(String name) {
        Table table = new Table();
        table.setName(SQLName.of(name));
        return table;
    }

    public static Table of(String name, String alias) {
        Table table = new Table();
        table.setName(SQLName.of(name));
        table.setAlias(alias);
        return table;
    }

    public static Table of(SQLName name) {
        return of(name, null);
    }

    public static Table of(SQLName name, String alias) {
        Table table = new Table();
        table.setName(name);
        table.setAlias(alias);
        return table;
    }

}
