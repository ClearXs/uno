package cc.allio.uno.data.orm.dsl.mongodb.dml;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.dml.InsertOperator;
import cc.allio.uno.data.orm.dsl.mongodb.MongodbSupport;
import cc.allio.uno.data.orm.dsl.type.DBType;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.bson.Document;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * mongodb insert document operator
 *
 * @author j.x
 * @since 1.1.7
 */
@AutoService(InsertOperator.class)
@Operator.Group(OperatorKey.MONGODB_LITERAL)
public class MongodbInsertOperator implements InsertOperator<MongodbInsertOperator> {

    @Getter
    private List<Document> docs;
    @Getter
    private List<DSLName> columns;
    private Table fromColl;

    public MongodbInsertOperator() {
        this.docs = Lists.newArrayList();
        this.columns = Lists.newArrayList();
    }

    @Override
    public String getDSL() {
        return MongodbSupport.toJson(docs);
    }

    @Override
    public MongodbInsertOperator parse(String dsl) {
        List<Document> documentsFromDsl = MongodbSupport.parse(dsl);
        this.docs.addAll(documentsFromDsl);
        return self();
    }

    @Override
    public MongodbInsertOperator customize(UnaryOperator<MongodbInsertOperator> operatorFunc) {
        return operatorFunc.apply(new MongodbInsertOperator());
    }

    @Override
    public void reset() {
        this.docs = Lists.newArrayList();
        this.columns = Lists.newArrayList();
        this.fromColl = null;
    }

    @Override
    public void setDBType(DBType dbType) {
        // nothing to do
    }

    @Override
    public DBType getDBType() {
        return DBType.MONGODB;
    }

    @Override
    public String getPrepareDSL() {
        return getDSL();
    }

    @Override
    public List<PrepareValue> getPrepareValues() {
        return List.of();
    }

    @Override
    public MongodbInsertOperator from(Table table) {
        this.fromColl = table;
        return self();
    }

    @Override
    public Table getTable() {
        return fromColl;
    }

    @Override
    public MongodbInsertOperator strictFill(String f, Supplier<Object> v) {
        for (Document doc : docs) {
            doc.put(f, v.get());
        }
        return self();
    }

    @Override
    public MongodbInsertOperator columns(Collection<DSLName> columns) {
        this.columns = Lists.newArrayList(columns);
        return self();
    }

    @Override
    public MongodbInsertOperator values(List<Object> values) {
        if (CollectionUtils.isEmpty(columns)) {
            return self();
        }
        Document document = new Document();
        for (int i = 0; i < columns.size(); i++) {
            DSLName col = columns.get(i);
            Object v = values.get(i);
            Object bsonValue = MongodbSupport.toBsonValue(v);
            document.put(col.format(), bsonValue);
        }
        this.docs.add(document);
        return self();
    }

    @Override
    public boolean isBatched() {
        return !this.docs.isEmpty();
    }
}
