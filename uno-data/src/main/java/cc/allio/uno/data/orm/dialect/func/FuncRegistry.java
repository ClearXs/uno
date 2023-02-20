package cc.allio.uno.data.orm.dialect.func;

import cc.allio.uno.data.orm.dialect.Dialect;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

/**
 * {@link FuncDescriptor} registry
 *
 * @author jiangwei
 * @date 2023/1/12 18:07
 * @since 1.1.4
 */
public class FuncRegistry {

    private final Map<String, FuncDescriptor> funcMap;

    public FuncRegistry() {
        this.funcMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    }

    /**
     * 获取Func的列表数据
     *
     * @return Func list
     */
    public List<FuncDescriptor> getFuncs() {
        return Lists.newArrayList(funcMap.values());
    }

    /**
     * 获取当前注册表所有的函数名称
     *
     * @return 函数名称List
     */
    public List<String> getFuncNames() {
        return Lists.newArrayList(funcMap.keySet());
    }

    /**
     * 向注册表中注册func实例
     *
     * @param funcName func name
     * @param func     func instance
     * @return FuncDescriptor
     */
    public FuncDescriptor register(String funcName, FuncDescriptor func) {
        return funcMap.put(funcName, func);
    }

    /**
     * 向func registry中注册FuncDescriptor实例
     *
     * @param func FuncDescriptor实例
     * @return FuncDescriptor
     */
    public FuncDescriptor register(FuncDescriptor func) {
        return register(func.getFuncName(), func);
    }

    /**
     * 从注册表中查找Func实例
     *
     * @param funcName func name
     * @return FuncDescriptor or null
     */
    public FuncDescriptor findFunc(String funcName) {
        return funcMap.get(funcName);
    }

    /**
     * {@link NamedFuncDescriptorBuilder}构造器
     *
     * @param funcName func name
     * @return NamedFuncDescriptorBuilder实例
     */
    public NamedFuncDescriptorBuilder namedFuncDescriptorBuilder(String funcName) {
        return NamedFuncDescriptorBuilder.builder().setFuncName(funcName);
    }

    public static class NamedFuncDescriptorBuilder {

        private String funcName;
        private String signature;
        private FuncType funcType;
        private Dialect dialect;
        private FuncRendering rendering;

        public static NamedFuncDescriptorBuilder builder() {
            return new NamedFuncDescriptorBuilder();
        }

        /**
         * 设置函数名称
         *
         * @param funcName func name
         * @return NamedFuncDescriptorBuilder
         */
        public NamedFuncDescriptorBuilder setFuncName(String funcName) {
            this.funcName = funcName;
            return this;
        }

        /**
         * 设置函数签名
         *
         * @param signature 签名
         * @return NamedFuncDescriptorBuilder
         */
        public NamedFuncDescriptorBuilder setSignature(String signature) {
            this.signature = signature;
            return this;
        }

        /**
         * 设置函数类型
         *
         * @param funcType 函数类型
         * @return NamedFuncDescriptorBuilder
         */
        public NamedFuncDescriptorBuilder setFuncType(FuncType funcType) {
            this.funcType = funcType;
            return this;
        }

        /**
         * 设置方言
         *
         * @param dialect dialect instance
         * @return NamedFuncDescriptorBuilder
         */
        public NamedFuncDescriptorBuilder setDialect(Dialect dialect) {
            this.dialect = dialect;
            return this;
        }

        /**
         * 设置func rendering
         *
         * @param rendering FuncRendering
         * @return NamedFuncDescriptorBuilder
         */
        public NamedFuncDescriptorBuilder setRendering(FuncRendering rendering) {
            this.rendering = rendering;
            return this;
        }

        /**
         * 设置func rendering
         *
         * @param rendering FuncRendering
         * @return NamedFuncDescriptorBuilder
         */
        public NamedFuncDescriptorBuilder setRendering(Supplier<FuncRendering> rendering) {
            this.rendering = rendering.get();
            return this;
        }

        /**
         * 构建 FuncDescriptor对象
         *
         * @return NamedFuncDescriptor
         */
        public NamedFuncDescriptor build() {
            return new NamedFuncDescriptor(funcName, signature, funcType, dialect, rendering);
        }
    }
}
