package cc.allio.uno.core.config;

/**
 * 刷新Spring配置文件
 *
 * @author jiangw
 * @date 2021/1/8 11:12
 * @see SpringValueRefresh
 * @see ConfigurationPropertiesRefresh
 * @since 1.0
 */
public interface ValueRefresh {

    /**
     * 当配置改变时，进行@Value的刷新，其内部实现是基于spring event模型，并且通过内部api实现
     *
     * @param bean     spring容器中的bean
     * @param beanName bean名称
     */
    void refresh(Object bean, String beanName);


    /**
     * 根据Spring 上下文刷新所有的bean Conguration
     *
     * @throws NoSuchMethodException 当没有找到bean时抛出异常
     */
    void refreshAll();
}
