package cc.allio.uno.gis.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(UnoGisProperties.class)
public class UnoGisAutoConfiguration {
}
