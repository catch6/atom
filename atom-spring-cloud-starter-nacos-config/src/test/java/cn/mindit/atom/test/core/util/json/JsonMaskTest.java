package cn.mindit.atom.test.core.util.json;

import cn.mindit.atom.core.util.MaskType;
import cn.mindit.atom.core.util.json.JsonMask;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JsonMaskTest {

    @JsonMask
    private String defaultField;

    @JsonMask(value = MaskType.CUSTOM, start = 2, end = 5)
    private String customField;

    @Test
    void defaultValues() throws NoSuchFieldException {
        JsonMask annotation = JsonMaskTest.class
            .getDeclaredField("defaultField")
            .getAnnotation(JsonMask.class);
        assertThat(annotation.value()).isEqualTo(MaskType.CUSTOM);
        assertThat(annotation.start()).isZero();
        assertThat(annotation.end()).isZero();
    }

    @Test
    void customValues() throws NoSuchFieldException {
        JsonMask annotation = JsonMaskTest.class
            .getDeclaredField("customField")
            .getAnnotation(JsonMask.class);
        assertThat(annotation.value()).isEqualTo(MaskType.CUSTOM);
        assertThat(annotation.start()).isEqualTo(2);
        assertThat(annotation.end()).isEqualTo(5);
    }

}
