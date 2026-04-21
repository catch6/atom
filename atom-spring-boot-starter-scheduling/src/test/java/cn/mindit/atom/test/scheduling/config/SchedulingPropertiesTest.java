package cn.mindit.atom.test.scheduling.config;

import cn.mindit.atom.scheduling.config.SchedulingProperties;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SchedulingPropertiesTest {

    @Test
    void enabledDefaultsToTrue() {
        SchedulingProperties properties = new SchedulingProperties();
        assertThat(properties.getEnabled()).isTrue();
    }

    @Test
    void enabledCanBeOverridden() {
        SchedulingProperties properties = new SchedulingProperties();
        properties.setEnabled(false);
        assertThat(properties.getEnabled()).isFalse();
    }

}
