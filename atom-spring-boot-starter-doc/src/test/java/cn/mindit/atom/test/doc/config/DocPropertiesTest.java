package cn.mindit.atom.test.doc.config;

import cn.mindit.atom.doc.config.DocProperties;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DocPropertiesTest {

    @Test
    void enabledDefaultsToTrue() {
        DocProperties properties = new DocProperties();
        assertThat(properties.isEnabled()).isTrue();
    }

    @Test
    void enabledCanBeSetToFalse() {
        DocProperties properties = new DocProperties();
        properties.setEnabled(false);
        assertThat(properties.isEnabled()).isFalse();
    }

}
