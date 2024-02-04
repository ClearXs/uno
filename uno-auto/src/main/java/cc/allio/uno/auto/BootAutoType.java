package cc.allio.uno.auto;

/**
 * 注解类型
 *
 * @author L.cm
 */
public enum BootAutoType {
    /**
     * 注解处理的类型
     */
    COMPONENT("org.springframework.stereotype.Component", "org.springframework.boot.autoconfigure.EnableAutoConfiguration");

    private final String annotationName;
    private final String configureKey;

    BootAutoType(String annotationName, String configureKey) {
        this.annotationName = annotationName;
        this.configureKey = configureKey;
    }

    public final String getAnnotationName() {
        return annotationName;
    }

    public final String getConfigureKey() {
        return configureKey;
    }

}
