package cn.mindit.atom.opc.da.config;

import cn.mindit.atom.opc.da.OpcDaListenerProcessor;
import cn.mindit.atom.opc.da.OpcDaService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.boot.autoconfigure.AutoConfiguration;

/**
 * @author Catch
 * @since 2024-06-21
 */
@Import({OpcDaConfiguration.class, OpcDaService.class, OpcDaListenerProcessor.class})
@EnableConfigurationProperties(OpcDaProperties.class)
@ConditionalOnProperty(value = "atom.opc.da.enabled", matchIfMissing = true)
@AutoConfiguration
public class OpcDaAutoConfiguration {

}
