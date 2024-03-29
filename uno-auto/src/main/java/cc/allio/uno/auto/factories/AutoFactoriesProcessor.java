package cc.allio.uno.auto.factories;

import cc.allio.uno.auto.AbstractUnoProcessor;
import cc.allio.uno.auto.BootAutoType;
import cc.allio.uno.auto.model.MultiSetMap;
import cc.allio.uno.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * spring boot 自动配置处理器
 *
 * @author L.cm
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes("*")
@SupportedOptions("debug")
public class AutoFactoriesProcessor extends AbstractUnoProcessor {
    /**
     * devtools，有 Configuration 注解的 jar 一般需要 devtools 配置文件
     */
    private static final String DEVTOOLS_RESOURCE_LOCATION = "META-INF/spring-devtools.properties";
    // Auto Configuration location
    private static final String AUTO_CONFIGURATION_LOCATION = "META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports";

    /**
     * 数据承载
     */
    private final MultiSetMap<String, String> factories = new MultiSetMap<>();
    /**
     * 元素辅助类
     */
    private Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    protected boolean processImpl(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            generateFactoriesFiles();
        } else {
            processAnnotations(annotations, roundEnv);
        }
        return false;
    }

    private void processAnnotations(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // 日志 打印信息 gradle build --debug
        log(annotations.toString());
        Set<? extends Element> elementSet = roundEnv.getRootElements();
        log("All Element set: " + elementSet.toString());

        // 过滤 TypeElement
        Set<TypeElement> typeElementSet = elementSet.stream()
                .filter(this::isClassOrInterface)
                .filter(TypeElement.class::isInstance)
                .map(TypeElement.class::cast)
                .collect(Collectors.toSet());
        // 如果为空直接跳出
        if (typeElementSet.isEmpty()) {
            log("Annotations elementSet is isEmpty");
            return;
        }

        for (TypeElement typeElement : typeElementSet) {
            for (BootAutoType autoType : BootAutoType.values()) {
                String annotation = autoType.getAnnotationName();
                if (isAnnotation(elementUtils, typeElement, annotation)) {
                    log("Found @" + annotation + " Element: " + typeElement.toString());

                    String factoryName = typeElement.getQualifiedName().toString();
                    if (factories.containsVal(factoryName)) {
                        continue;
                    }

                    log("读取到新配置 spring.factories factoryName：" + factoryName);
                    factories.put(autoType.getConfigureKey(), factoryName);
                }
            }
        }
    }

    private void generateFactoriesFiles() {
        if (factories.isEmpty()) {
            return;
        }
        Filer filer = processingEnv.getFiler();
        try {
            // 1. auto configuration file
            FileObject autoConfigurationFile = filer.createResource(StandardLocation.CLASS_OUTPUT, "", AUTO_CONFIGURATION_LOCATION);
            FactoriesFiles.writeFactoriesFile(factories, autoConfigurationFile.openOutputStream());
            String classesPath = autoConfigurationFile.toUri().toString().split("classes")[0];
            Path projectPath = Paths.get(new URI(classesPath)).getParent();
            // 2. devtools 配置，因为有 @Configuration 注解的需要 devtools
            String projectName = projectPath.getFileName().toString();
            FileObject devToolsFile = filer.createResource(StandardLocation.CLASS_OUTPUT, "", DEVTOOLS_RESOURCE_LOCATION);
            FactoriesFiles.writeDevToolsFile(projectName, devToolsFile.openOutputStream());
        } catch (IOException | URISyntaxException e) {
            fatalError(e);
        }
    }

    private boolean isClassOrInterface(Element e) {
        ElementKind kind = e.getKind();
        return kind == ElementKind.CLASS || kind == ElementKind.INTERFACE;
    }

    private boolean isAnnotation(Elements elementUtils, Element e, String annotationFullName) {
        List<? extends AnnotationMirror> annotationList = elementUtils.getAllAnnotationMirrors(e);
        for (AnnotationMirror annotation : annotationList) {
            // 如果是对于的注解
            if (isAnnotation(annotationFullName, annotation)) {
                return true;
            }
            // 处理组合注解
            Element element = annotation.getAnnotationType().asElement();
            // 如果是 java 元注解，继续循环
            if (element.toString().startsWith("java.lang")) {
                continue;
            }
            // 递归处理 组合注解
            if (isAnnotation(elementUtils, element, annotationFullName)) {
                return true;
            }
        }
        return false;
    }

    private boolean isAnnotation(String annotationFullName, AnnotationMirror annotation) {
        return annotationFullName.equals(annotation.getAnnotationType().toString());
    }
}
