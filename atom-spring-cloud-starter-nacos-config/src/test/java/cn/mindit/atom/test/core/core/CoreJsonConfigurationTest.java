package cn.mindit.atom.test.core.core;

import cn.mindit.atom.core.core.CoreJsonConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;

import static org.assertj.core.api.Assertions.assertThat;

class CoreJsonConfigurationTest {

    @Test
    void producesJsonMapperBuilderCustomizerBean() {
        CoreJsonConfiguration config = new CoreJsonConfiguration();
        JsonMapperBuilderCustomizer customizer = config.jsonMapperBuilderCustomizer();
        assertThat(customizer).isNotNull();
    }

}
