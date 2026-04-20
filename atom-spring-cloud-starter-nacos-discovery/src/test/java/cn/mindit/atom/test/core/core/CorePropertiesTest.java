package cn.mindit.atom.test.core.core;

import cn.mindit.atom.core.core.CoreProperties;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CorePropertiesTest {

    @Test
    void allFlagsDefaultToTrue() {
        CoreProperties properties = new CoreProperties();
        assertThat(properties.isEnabled()).isTrue();
        assertThat(properties.isTrace()).isTrue();
        assertThat(properties.isJson()).isTrue();
    }

    @Test
    void flagsCanBeOverridden() {
        CoreProperties properties = new CoreProperties();
        properties.setEnabled(false);
        properties.setTrace(false);
        properties.setJson(false);
        assertThat(properties.isEnabled()).isFalse();
        assertThat(properties.isTrace()).isFalse();
        assertThat(properties.isJson()).isFalse();
    }

}
