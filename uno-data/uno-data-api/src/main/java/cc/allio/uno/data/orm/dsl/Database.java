package cc.allio.uno.data.orm.dsl;

import lombok.Data;

/**
 * 数据库信息
 *
 * @author j.x
 * @since 1.1.7
 */
@Data
public class Database implements Meta<Database> {

    /**
     * 数据库名
     */
    private DSLName name;

    public Database() {

    }

    public Database(DSLName dslName) {
        this.name = dslName;
    }

    /**
     * of {@link Database} instance
     *
     * @param dslName the dsl name
     * @return {@link Database} instance
     */
    public static Database of(DSLName dslName) {
        Database database = new Database();
        database.setName(dslName);
        return database;
    }
}
