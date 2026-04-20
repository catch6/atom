package cn.mindit.atom.opc.ua.config;

import cn.mindit.atom.opc.ua.OpcUaListenerProcessor;
import cn.mindit.atom.opc.ua.OpcUaService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.boot.autoconfigure.AutoConfiguration;

/**
 * @author Catch
 * @since 2024-06-23
 */
@Import({OpcUaConfiguration.class, OpcUaService.class, OpcUaListenerProcessor.class})
@EnableConfigurationProperties(OpcUaProperties.class)
@ConditionalOnProperty(value = "atom.opc.ua.enabled", matchIfMissing = true)
@AutoConfiguration
public class OpcUaAutoConfiguration {

}
