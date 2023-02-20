package cc.allio.uno.data.orm.dialect;

import cc.allio.uno.data.orm.dialect.func.FuncRegistry;
import cc.allio.uno.data.orm.dialect.func.ReuseFuncFactory;
import cc.allio.uno.data.orm.dialect.type.*;

import java.sql.Types;

/**
 * dialect抽象层
 *
 * @author jiangwei
 * @date 2023/1/5 18:49
 * @since 1.1.4
 */
public abstract class AbstractDialect implements Dialect {

    protected final TypeRegistry typeRegistry;
    protected final FuncRegistry funcRegistry;

    protected AbstractDialect() {
        this.funcRegistry = new FuncRegistry();
        // registry general func
        funcRegistry.register(ReuseFuncFactory.count(this));
        funcRegistry.register(ReuseFuncFactory.min(this));
        funcRegistry.register(ReuseFuncFactory.max(this));
        funcRegistry.register(ReuseFuncFactory.avg(this));
        funcRegistry.register(ReuseFuncFactory.sum(this));
        // 触发子类 func
        triggerInitFunc();

        // registry general type
        this.typeRegistry = new TypeRegistry();

        // numeric types
        typeRegistry.register(
                JdbcType.builder()
                        .setJdbcCode(Types.BOOLEAN)
                        .setName("boolean")
                        .build(),
                new BooleanJavaType()
        );
        // smallint
        typeRegistry.register(
                JdbcType.builder()
                        .setJdbcCode(Types.SMALLINT)
                        .setSignature("smallint($m)")
                        .setMaxBytes(2L)
                        .build(),
                new ShortJavaType()
        );
        // int
        typeRegistry.register(
                JdbcType.builder()
                        .setJdbcCode(Types.INTEGER)
                        .setSignature("int($m)")
                        .setMaxBytes(4L)
                        .build(),
                new IntegerJavaType()
        );
        // bigint
        typeRegistry.register(
                JdbcType.builder()
                        .setJdbcCode(Types.BIGINT)
                        .setSignature("bigint($m)")
                        .setMaxBytes(8L)
                        .build(),
                new LongJavaType()
        );
        // decimal
        typeRegistry.register(
                JdbcType.builder()
                        .setJdbcCode(Types.DECIMAL)
                        .setSignature("decimal($p,$s)")
                        .isDecimal(true)
                        .setMaxBytes(8L)
                        .build(),
                new BigDecimalJavaType()
        );
        // numeric
        typeRegistry.register(
                JdbcType.builder()
                        .setJdbcCode(Types.NUMERIC)
                        .setSignature("numeric($p,$s)")
                        .isDecimal(true)
                        .setMaxBytes(8L)
                        .build(),
                new BigDecimalJavaType()
        );
        // varchar
        typeRegistry.register(
                JdbcType.builder()
                        .setJdbcCode(Types.VARCHAR)
                        .setSignature("varchar($n)")
                        .build(),
                new StringJavaType()
        );
        // 初始化type
        triggerInitType();
    }

    @Override
    public FuncRegistry getFuncRegistry() {
        return funcRegistry;
    }

    @Override
    public TypeRegistry getTypeRegistry() {
        return typeRegistry;
    }

    /**
     * 触发初始化Func实例
     */
    protected abstract void triggerInitFunc();

    /**
     * 触发初始化Type实例
     */
    protected abstract void triggerInitType();
}
