package cc.allio.uno.data.orm.dsl.mongodb.dml;

import cc.allio.uno.core.type.Types;
import cc.allio.uno.core.util.Values;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.WhereOperator;
import cc.allio.uno.data.orm.dsl.logical.BiLogical;
import cc.allio.uno.data.orm.dsl.logical.Logical;
import com.google.common.collect.Lists;
import com.mongodb.client.model.Filters;
import lombok.Getter;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@link WhereOperator} default implementation for mongodb
 *
 * @author j.x
 * @date 2024/3/12 01:22
 * @since 1.1.7
 */
public class MongodbWhereOperatorImpl<T extends WhereOperator<T>> implements WhereOperator<T> {

    @Getter
    protected Bson filter;
    private MongodbLogical logical;
    private List<Bson> expression;

    public MongodbWhereOperatorImpl() {
        this.filter = Filters.empty();
        this.expression = Lists.newArrayList();
        this.logical = new MongodbAndLogical();
    }

    @Override
    public T gt(DSLName dslName, Object value) {
        Bson expr = Filters.gt(dslName.format(), value);
        this.expression.add(expr);
        this.filter = logical.doAccept(expression);
        return self();
    }

    @Override
    public T gte(DSLName dslName, Object value) {
        Bson expr = Filters.gte(dslName.format(), value);
        this.expression.add(expr);
        this.filter = logical.doAccept(expression);
        return self();
    }

    @Override
    public T lt(DSLName dslName, Object value) {
        Bson expr = Filters.lt(dslName.format(), value);
        this.expression.add(expr);
        this.filter = logical.doAccept(expression);
        return self();
    }

    @Override
    public T lte(DSLName dslName, Object value) {
        Bson expr = Filters.lte(dslName.format(), value);
        this.expression.add(expr);
        this.filter = logical.doAccept(expression);
        return self();
    }

    @Override
    public T eq(DSLName dslName, Object value) {
        Bson expr = Filters.eq(dslName.format(), value);
        this.expression.add(expr);
        this.filter = logical.doAccept(expression);
        return self();
    }

    @Override
    public T neq(DSLName dslName, Object value) {
        Bson expr = Filters.ne(dslName.format(), value);
        this.expression.add(expr);
        this.filter = logical.doAccept(expression);
        return self();
    }

    @Override
    public T notNull(DSLName dslName) {
        Bson expr = Filters.ne(dslName.format(), null);
        this.expression.add(expr);
        this.filter = logical.doAccept(expression);
        return self();
    }

    @Override
    public T isNull(DSLName dslName) {
        Bson expr = Filters.eq(dslName.format(), null);
        this.expression.add(expr);
        this.filter = logical.doAccept(expression);
        return self();
    }

    @Override
    public <V> T in(DSLName dslName, V... values) {
        Bson expr = Filters.in(dslName.format(), Values.expand(values));
        this.expression.add(expr);
        this.filter = logical.doAccept(expression);
        return self();
    }

    @Override
    public <V> T notIn(DSLName dslName, V... values) {
        Bson expr = Filters.nin(dslName.format(), Values.expand(values));
        this.expression.add(expr);
        this.filter = logical.doAccept(expression);
        return self();
    }

    @Override
    public T between(DSLName dslName, Object withValue, Object endValue) {
        Bson expr = Filters.in(dslName.format(), withValue, endValue);
        this.expression.add(expr);
        this.filter = logical.doAccept(expression);
        return self();
    }

    @Override
    public T notBetween(DSLName dslName, Object withValue, Object endValue) {
        Bson expr = Filters.nin(dslName.format(), withValue, endValue);
        this.expression.add(expr);
        this.filter = logical.doAccept(expression);
        return self();
    }

    @Override
    public T like(DSLName dslName, Object value) {
        Bson expr = Filters.regex(dslName.format(), Types.toString(value), "im");
        this.expression.add(expr);
        this.filter = logical.doAccept(expression);
        return self();
    }

    @Override
    public T $like(DSLName dslName, Object value) {
        Bson expr = Filters.regex(dslName.format(), "^" + Types.toString(value), "im");
        this.expression.add(expr);
        this.filter = logical.doAccept(expression);
        return self();
    }

    @Override
    public T like$(DSLName dslName, Object value) {
        Bson expr = Filters.regex(dslName.format(), Types.toString(value) + "$", "im");
        this.expression.add(expr);
        this.filter = logical.doAccept(expression);
        return self();
    }

    @Override
    public T $like$(DSLName dslName, Object value) {
        Bson expr = Filters.regex(dslName.format(), Types.toString(value), "im");
        this.expression.add(expr);
        this.filter = logical.doAccept(expression);
        return self();
    }

    @Override
    public T notLike(DSLName dslName, Object value) {
        Bson expr = Filters.regex(dslName.format(), Types.toString(value), "im");
        Bson notLikeExpr = Filters.not(expr);
        this.expression.add(notLikeExpr);
        this.filter = logical.doAccept(expression);
        return self();
    }

    @Override
    public T $notLike(DSLName dslName, Object value) {
        Bson expr = Filters.regex(dslName.format(), "^" + Types.toString(value), "im");
        Bson notLikeExpr = Filters.not(expr);
        this.expression.add(notLikeExpr);
        this.filter = logical.doAccept(expression);
        return self();
    }

    @Override
    public T notLike$(DSLName dslName, Object value) {
        Bson expr = Filters.regex(dslName.format(), Types.toString(value) + "$", "im");
        Bson notLikeExpr = Filters.not(expr);
        this.expression.add(notLikeExpr);
        this.filter = logical.doAccept(expression);
        return self();
    }

    @Override
    public T $notLike$(DSLName dslName, Object value) {
        Bson expr = Filters.regex(dslName.format(), Types.toString(value), "im");
        Bson notLikeExpr = Filters.not(expr);
        this.expression.add(notLikeExpr);
        this.filter = logical.doAccept(expression);
        return self();
    }

    @Override
    public T or() {
        // 采用 this.expression.clear() 在重新添加filter，将会报下面错误：
        // *** java.lang.instrument ASSERTION FAILED ***: "!errorOutstanding" with message transform method call failed at open/src/java.instrument/share/native/libinstrument/JPLISAgent.c line: 884
        // 并且将会产生StackOverflow
        // 没有找到解决原因 https://stackoverflow.com/questions/60526670/what-is-the-meaning-cause-of-java-lang-instrument-assertion-failed-er
        this.expression = Lists.newArrayList();
        this.expression.add(filter);
        this.logical = new MongodbOrLogical();
        this.filter = this.logical.doAccept(expression);
        return self();
    }

    @Override
    public T and() {
        this.expression = Lists.newArrayList();
        this.expression.add(filter);
        this.logical = new MongodbAndLogical();
        this.filter = this.logical.doAccept(expression);
        return self();
    }

    @Override
    public T nor() {
        this.expression = Lists.newArrayList();
        this.expression.add(filter);
        this.logical = new MongodbNorLogical();
        this.filter = this.logical.doAccept(expression);
        return self();
    }

    protected void clear() {
        this.filter = null;
        this.logical = new MongodbAndLogical();
        this.expression = new ArrayList<>();
    }

    interface MongodbLogical extends BiLogical<Bson, List<Bson>> {

    }

    // mongodb logical syntax 'or' default implementation
    static class MongodbOrLogical implements MongodbLogical {

        @Override
        public Bson doAccept(List<Bson> p) {
            return Filters.or(p);
        }

        @Override
        public Logical getLogical() {
            return Logical.OR;
        }
    }

    // mongodb logical syntax 'and' default implementation
    static class MongodbAndLogical implements MongodbLogical {
        @Override
        public Bson doAccept(List<Bson> p) {
            return Filters.and(p);
        }

        @Override
        public Logical getLogical() {
            return Logical.AND;
        }
    }

    // mongodb logical syntax 'nor' default implementation
    static class MongodbNorLogical implements MongodbLogical {

        @Override
        public Bson doAccept(List<Bson> p) {
            return Filters.nor(p);
        }

        @Override
        public Logical getLogical() {
            return Logical.NOR;
        }
    }
}
