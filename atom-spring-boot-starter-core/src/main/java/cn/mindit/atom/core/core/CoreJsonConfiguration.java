package cn.mindit.atom.core.core;

import cn.mindit.atom.core.util.JsonUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Catch
 * @since 2023-06-06
 */
@Configuration
@ConditionalOnProperty(value = "atom.core.json", matchIfMissing = true)
public class CoreJsonConfiguration {

    @Bean
    public JsonMapperBuilderCustomizer jsonMapperBuilderCustomizer() {
        return JsonUtils.customize();
    }

}
