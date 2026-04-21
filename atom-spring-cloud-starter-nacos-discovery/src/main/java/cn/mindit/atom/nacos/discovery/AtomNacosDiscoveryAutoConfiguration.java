package cn.mindit.atom.nacos.discovery;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Catch
 * @since 2023-08-13
 */
@PropertySource("classpath:application-nacos-discovery.properties")
@AutoConfiguration
public class AtomNacosDiscoveryAutoConfiguration {

}
