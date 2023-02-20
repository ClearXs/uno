package cc.allio.uno.data.orm.dialect.func;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.data.orm.dialect.Dialect;
import cc.allio.uno.data.sql.word.KeyWords;
import cc.allio.uno.data.sql.Alias;
import cc.allio.uno.data.sql.RuntimeColumn;
import cc.allio.uno.data.sql.word.Distinct;
import cc.allio.uno.data.sql.word.KeyWord;

import static cc.allio.uno.data.orm.dialect.func.FuncRendering.getSingleArgument;

/**
 * 对于不同数据库而言，即使存在差异，也还有可以复用的部分。当前类把那些可以进行复用的函数进行维护
 *
 * @author jiangwei
 * @date 2023/1/12 19:06
 * @since 1.1.4
 */
public class ReuseFuncFactory {

    private ReuseFuncFactory() {
    }

    /**
     * count func
     *
     * @param dialect dialect instance
     * @return FuncDescriptor instance
     * @see <a href="https://dev.mysql.com/doc/refman/8.0/en/aggregate-functions.html#function_count">mysql</a>
     * @see <a href="https://learn.microsoft.com/en-us/sql/t-sql/functions/count-transact-sql?view=sql-server-ver16">mssql</a>
     * @see <a href="https://www.postgresql.org/docs/15/functions-aggregate.html">pgsql</a>
     */
    public static FuncDescriptor count(Dialect dialect) {
        return dialect.getFuncRegistry()
                .namedFuncDescriptorBuilder(Func.COUNT_FUNCTION)
                .setFuncType(FuncType.AGGREGATE)
                .setDialect(dialect)
                .setSignature("COUNT(expr) [over_clause]")
                .setRendering(GeneralAggregateFunctionRendering::new)
                .build();
    }

    /**
     * min func
     *
     * @param dialect dialect instance
     * @return FuncDescriptor instance
     * @see <a href="https://dev.mysql.com/doc/refman/8.0/en/aggregate-functions.html#function_min">mysql</a>
     * @see <a href="https://learn.microsoft.com/en-us/sql/t-sql/functions/min-transact-sql?view=sql-server-ver16">mssql</a>
     * @see <a href="https://www.postgresql.org/docs/15/functions-aggregate.html">pgsql</a>
     */
    public static FuncDescriptor min(Dialect dialect) {
        return dialect.getFuncRegistry()
                .namedFuncDescriptorBuilder(Func.MIN_FUNCTION)
                .setFuncType(FuncType.AGGREGATE)
                .setDialect(dialect)
                .setSignature("MIN([DISTINCT] expr) [over_clause]")
                .setRendering(GeneralAggregateFunctionRendering::new)
                .build();
    }

    /**
     * max func
     *
     * @param dialect dialect instance
     * @return FuncDescriptor instance
     * @see <a href="https://dev.mysql.com/doc/refman/8.0/en/aggregate-functions.html#function_max">mysql</a>
     * @see <a href="https://learn.microsoft.com/en-us/sql/t-sql/functions/max-transact-sql?view=sql-server-ver16">mssql</a>
     * @see <a href="https://www.postgresql.org/docs/15/functions-aggregate.html">pgsql</a>
     */
    public static FuncDescriptor max(Dialect dialect) {
        return dialect.getFuncRegistry()
                .namedFuncDescriptorBuilder(Func.MAX_FUNCTION)
                .setFuncType(FuncType.AGGREGATE)
                .setDialect(dialect)
                .setSignature("MAX([DISTINCT] expr) [over_clause]")
                .setRendering(GeneralAggregateFunctionRendering::new)
                .build();
    }

    /**
     * avg func
     *
     * @param dialect dialect instance
     * @return FuncDescriptor instance
     * @see <a href="https://dev.mysql.com/doc/refman/8.0/en/aggregate-functions.html#function_avg">mysql</a>
     * @see <a href="https://learn.microsoft.com/en-us/sql/t-sql/functions/avg-transact-sql?view=sql-server-ver16">mssql</a>
     * @see <a href="https://www.postgresql.org/docs/15/functions-aggregate.html">pgsql</a>
     */
    public static FuncDescriptor avg(Dialect dialect) {
        return dialect.getFuncRegistry()
                .namedFuncDescriptorBuilder(Func.AVG_FUNCTION)
                .setFuncType(FuncType.AGGREGATE)
                .setDialect(dialect)
                .setSignature("AVG([DISTINCT] expr) [over_clause]")
                .setRendering(GeneralAggregateFunctionRendering::new)
                .build();
    }

    /**
     * sum func
     *
     * @param dialect dialect instance
     * @return FuncDescriptor
     * @see <a href="https://dev.mysql.com/doc/refman/8.0/en/aggregate-functions.html#function_sum">mysql</a>
     * @see <a href="https://learn.microsoft.com/en-us/sql/t-sql/functions/sum-transact-sql?view=sql-server-ver16">mssql</a>
     * @see <a href="https://www.postgresql.org/docs/15/functions-aggregate.html">pgsql</a>
     */
    public static FuncDescriptor sum(Dialect dialect) {
        return dialect.getFuncRegistry()
                .namedFuncDescriptorBuilder(Func.SUM_FUNCTION)
                .setFuncType(FuncType.AGGREGATE)
                .setDialect(dialect)
                .setSignature("AVG([DISTINCT] expr) [over_clause]")
                .setRendering(GeneralAggregateFunctionRendering::new)
                .build();
    }

    /**
     * 通用的AggregateFunction
     */
    static class GeneralAggregateFunctionRendering implements FuncRendering {

        @Override
        public String render(FuncDescriptor descriptor, Object[] arguments) {
            KeyWord keyWord = KeyWords.get(descriptor.getFuncName());
            StringBuilder sql = new StringBuilder();
            sql.append(keyWord.get()).append(StringPool.LEFT_BRACKET);
            getSingleArgument(arguments, Distinct.class).ifPresent(distinct -> sql.append(distinct.get()).append(StringPool.SPACE));
            getSingleArgument(arguments, RuntimeColumn.class).ifPresent(column -> sql.append(column.getName()));
            sql.append(StringPool.RIGHT_BRACKET);
            getSingleArgument(arguments, Alias.class).ifPresent(alias -> sql.append(StringPool.SPACE).append(alias.getAlias()));
            return sql.toString();
        }
    }
}
