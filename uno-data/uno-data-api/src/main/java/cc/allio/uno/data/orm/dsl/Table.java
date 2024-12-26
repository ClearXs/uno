package cc.allio.uno.data.orm.dsl;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 表
 *
 * @author j.x
 * @since 1.1.4
 */
@Data
@Accessors(chain = true)
public class Table implements Meta<Table> {

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

    public Table() {
    }

    public Table(String name) {
        this.name = DSLName.of(name);
    }

    public Table(String name, String alias) {
        this.name = DSLName.of(name);
        this.alias = alias;
    }

    public Table(DSLName dslName) {
        this.name = dslName;
    }

    public Table(DSLName dslName, String alias) {
        this.name = dslName;
        this.alias = alias;
    }

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

    /**
     * create {@link Table} by table name and table alias
     *
     * @param name  the name
     * @param alias the alias
     * @return table instance
     */
    public static Table of(String name, String alias) {
        Table table = of(name);
        table.setAlias(alias);
        return table;
    }

    /**
     * create {@link Table} by table {@link DSLName}
     *
     * @param name the dsl name
     * @return table instance
     */
    public static Table of(DSLName name) {
        return of(name, null);
    }

    /**
     * creat {@link Table} by table {@link DSLName} and table alias
     *
     * @param name  the name
     * @param alias the alias
     * @return table instance
     */
    public static Table of(DSLName name, String alias) {
        Table table = new Table();
        table.setName(name);
        table.setAlias(alias);
        return table;
    }
}
