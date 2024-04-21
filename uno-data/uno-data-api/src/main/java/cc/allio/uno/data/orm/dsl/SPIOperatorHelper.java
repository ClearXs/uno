package cc.allio.uno.data.orm.dsl;

import cc.allio.uno.core.function.SupplierAction;
import cc.allio.uno.core.function.VoidConsumer;
import cc.allio.uno.core.util.ClassUtils;
import cc.allio.uno.data.orm.dsl.exception.DSLException;
import cc.allio.uno.data.orm.dsl.type.DBType;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Helper
 *
 * @author j.x
 * @date 2024/1/3 22:25
 * @since 1.1.7
 */
@Slf4j
public final class SPIOperatorHelper {

    private static final Map<OperatorKey, OperatorTraitGroup> OTG_CACHES = Maps.newConcurrentMap();
    private static final ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * @see #lazyGet(Class, OperatorKey, DBType)
     */
    public static <T extends Operator<?>> T lazyGet(Class<T> operatorClass, OperatorKey operatorKey) {
        return lazyGet(operatorClass, operatorKey, null);
    }

    /**
     * 懒加载获取{@link Operator}实例
     *
     * @param operatorClass operatorClass
     * @param operatorKey   operator 分组key
     * @param dbType        dbType maybe null
     * @param <T>           <T>
     * @return instance or null
     * @throws DSLException             当通过SPI没有找到获取实例化失败时抛出
     * @throws IllegalArgumentException operatorClass or groupKey为null时抛出
     */
    public static <T extends Operator<?>> T lazyGet(Class<T> operatorClass, OperatorKey operatorKey, DBType dbType) {
        if (operatorClass == null || operatorKey == null) {
            throw new IllegalArgumentException("The parameter operatorClass or groupKey is null");
        }
        return ifNullThenAgain(
                () -> {
                    OperatorTraitGroup operatorTraitGroup = OTG_CACHES.get(operatorKey);
                    OperatorTrait operatorTrait;
                    Lock readLock = lock.readLock();
                    try {
                        readLock.lock();
                        operatorTrait = operatorTraitGroup.find(operatorClass);
                        if (operatorTrait == null) {
                            throw new DSLException(
                                    String.format(
                                            "On the basis of SPI load operator %s and %s, " +
                                            "but not found counterpart Impl, " +
                                            "Please Check the Impl Has Annotation @AutoService", operatorKey.key(), operatorClass.getName()));
                        }
                    } finally {
                        readLock.unlock();
                    }
                    return operatorTrait.newInstance(dbType);
                },
                () -> loadOperatorBySPI(operatorClass),
                null);
    }

    /**
     * 当第一次执行action时，如果失败或者抛出异常首先会执行'补偿'机制后，然后再次执行，如果第二次执行如果还是为null，则根据第一次执行的是否有异常（如果参数异常不为null）则抛出
     *
     * @param action     action
     * @param compensate compensate
     * @param failErr    执行失败异常信息
     * @return 如果执行不为null则返回，或者没有异常则返回null
     * @throws DSLException 包装异常信息
     */
    private static <T> T ifNullThenAgain(SupplierAction<T> action, VoidConsumer compensate, Throwable failErr) {
        Throwable err = null;
        T result = null;
        try {
            result = action.get();
        } catch (Throwable ex) {
            err = ex;
        }
        if (result == null) {
            try {
                compensate.doAccept();
                result = action.get();
            } catch (Throwable ex) {
                err = ex;
            }
        }
        if (result != null) {
            return result;
        }
        if (failErr != null) {
            err = failErr;
        }
        if (err != null) {
            throw new DSLException(err);
        }
        return null;
    }

    /**
     * 基于SPI加载{@link Operator}的类型，放入缓存
     */
    private static void loadOperatorBySPI(Class<? extends Operator<?>> operatorClass) {
        ServiceLoader<? extends Operator<?>> loader = ServiceLoader.load(operatorClass, ClassLoader.getSystemClassLoader());
        boolean present = loader.stream().findAny().isPresent();
        if (!present) {
            Class<?>[] interfaces = operatorClass.getInterfaces();
            for (Class<?> hierachical : interfaces) {
                // recursion of find parent class
                // requirement parent class must be interfaces and subtype of Operator
                if (Operator.class.isAssignableFrom(hierachical)) {
                    loadOperatorBySPI((Class<? extends Operator<?>>) hierachical);
                    break;
                }
            }
        } else {
            loader.reload();
            loader.stream()
                    .forEach(provider -> {
                        Class<? extends Operator<?>> type = provider.type();
                        Operator.Group group = AnnotationUtils.findAnnotation(type, Operator.Group.class);
                        if (group == null) {
                            throw new IllegalArgumentException(String.format("Operator %s without Annotation @SQLOperator.Group", type.getName()));
                        }
                        String groupKey = group.value();
                        Lock writeLock = lock.writeLock();
                        writeLock.lock();
                        try {
                            OperatorTraitGroup operatorTraitGroup = OTG_CACHES.computeIfAbsent(OperatorKey.returnKey(groupKey), k -> new OperatorTraitGroup());
                            if (log.isDebugEnabled()) {
                                log.debug("Through SPI load Operator Type [{}]", type.getName());
                            }
                            operatorTraitGroup.append(type);
                        } finally {
                            writeLock.unlock();
                        }
                    });
        }
    }

    static class OperatorTraitGroup {

        private final Set<OperatorTrait> traits;

        public OperatorTraitGroup() {
            this.traits = Sets.newConcurrentHashSet();
        }

        /**
         * by the operator class find OperatorTrait
         *
         * @param operatorClazz operatorClazz
         * @return OperatorTrait or null
         */
        public OperatorTrait find(Class<? extends Operator<?>> operatorClazz) {
            // find operator class parent
            Class<? extends Operator<?>> parent = Operator.getHierarchicalType(operatorClazz);
            return this.traits.stream()
                    .filter(trait -> trait.getParent().equals(parent))
                    .findFirst()
                    .orElse(null);
        }

        /**
         * @see #append(OperatorTrait)
         */
        public void append(Class<? extends Operator<?>> implClazz) {
            this.append(new OperatorTrait(implClazz));
        }

        /**
         * with group append {@link OperatorTrait} instance
         *
         * @param trait trait must not nul
         */
        public void append(OperatorTrait trait) {
            if (trait == null) {
                throw new IllegalArgumentException("parameter OperatorTrait is null");
            }
            if (this.traits.contains(trait)) {
                throw new IllegalArgumentException(String.format("repetitive operator %s, make sure only one operator impl by specific OperatorKey", trait.getClazz().getName()));
            }
            this.traits.add(trait);
        }
    }

    @Data
    @EqualsAndHashCode(of = "parent")
    static class OperatorTrait {

        private final Class<? extends Operator<?>> clazz;
        private Class<? extends Operator<?>> parent;

        public OperatorTrait(Class<? extends Operator<?>> clazz) {
            this.clazz = clazz;
            this.parent = Operator.getHierarchicalType(clazz);
            if (this.parent == null) {
                throw new DSLException(String.format("operator impl %s not implement any Operator", clazz.getName()));
            }
        }

        /**
         * new Operator instance by dbtype
         *
         * @param dbType dbType
         * @param <T>    generic Operator type
         * @return Operator instance
         */
        public <T extends Operator<?>> T newInstance(DBType dbType) {
            Operator<?> sqlOperator;
            try {
                if (dbType == null) {
                    sqlOperator = ClassUtils.newInstance(clazz);
                } else {
                    sqlOperator = ClassUtils.newInstance(clazz, dbType);
                }
            } catch (Throwable ex) {
                throw new DSLException(String.format("Instance Operator %s failed, please invoke method Is it right?", clazz.getName()), ex);
            }
            return (T) sqlOperator;
        }
    }
}
