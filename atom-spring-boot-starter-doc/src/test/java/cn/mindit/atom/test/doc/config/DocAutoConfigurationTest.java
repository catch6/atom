package cn.mindit.atom.test.doc.config;

import cn.mindit.atom.doc.config.DocAutoConfiguration;
import cn.mindit.atom.doc.config.DocProperties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class DocAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(DocAutoConfiguration.class));

    @Test
    void autoConfigurationLoadsWhenPropertyNotSet() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(DocAutoConfiguration.class);
            assertThat(context).hasSingleBean(DocProperties.class);
        });
    }

    @Test
    void autoConfigurationLoadsWhenExplicitlyEnabled() {
        contextRunner
                .withPropertyValues("atom.doc.enabled=true")
                .run(context -> {
                    assertThat(context).hasSingleBean(DocAutoConfiguration.class);
                    assertThat(context).hasSingleBean(DocProperties.class);
                });
    }

    @Test
    void autoConfigurationDisabledWhenPropertySetToFalse() {
        contextRunner
                .withPropertyValues("atom.doc.enabled=false")
                .run(context -> {
                    assertThat(context).doesNotHaveBean(DocAutoConfiguration.class);
                    assertThat(context).doesNotHaveBean(DocProperties.class);
                });
    }

    @Test
    void docPropertiesEnabledDefaultsToTrue() {
        contextRunner.run(context -> {
            DocProperties properties = context.getBean(DocProperties.class);
            assertThat(properties.isEnabled()).isTrue();
        });
    }

    @Test
    void springdocDefaultPropertiesApplied() {
        contextRunner.run(context -> {
            assertThat(context.getEnvironment().getProperty("springdoc.use-fqn"))
                    .isEqualTo("true");
            assertThat(context.getEnvironment().getProperty("springdoc.override-with-generic-response"))
                    .isEqualTo("false");
            assertThat(context.getEnvironment().getProperty("springdoc.writer-with-default-pretty-printer"))
                    .isEqualTo("true");
            assertThat(context.getEnvironment().getProperty("springdoc.default-produces-media-type"))
                    .isEqualTo("application/json");
            assertThat(context.getEnvironment().getProperty("springdoc.swagger-ui.display-request-duration"))
                    .isEqualTo("true");
        });
    }

    @Test
    void springdocDefaultPropertiesCanBeOverridden() {
        contextRunner
                .withPropertyValues(
                        "springdoc.use-fqn=false",
                        "springdoc.default-produces-media-type=text/plain"
                )
                .run(context -> {
                    assertThat(context.getEnvironment().getProperty("springdoc.use-fqn"))
                            .isEqualTo("false");
                    assertThat(context.getEnvironment().getProperty("springdoc.default-produces-media-type"))
                            .isEqualTo("text/plain");
                });
    }

}
