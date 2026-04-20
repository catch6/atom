package cn.mindit.atom.test.core.util.json;

import cn.mindit.atom.core.util.json.JsonDecimalFormat;
import org.junit.jupiter.api.Test;

import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;

class JsonDecimalFormatTest {

    @JsonDecimalFormat
    private String defaultField;

    @JsonDecimalFormat(value = "0.###", roundingMode = RoundingMode.HALF_DOWN)
    private String customField;

    @Test
    void defaultValueAndRoundingMode() throws NoSuchFieldException {
        JsonDecimalFormat annotation = JsonDecimalFormatTest.class
            .getDeclaredField("defaultField")
            .getAnnotation(JsonDecimalFormat.class);
        assertThat(annotation.value()).isEqualTo("0.00");
        assertThat(annotation.roundingMode()).isEqualTo(RoundingMode.HALF_UP);
    }

    @Test
    void customValueAndRoundingMode() throws NoSuchFieldException {
        JsonDecimalFormat annotation = JsonDecimalFormatTest.class
            .getDeclaredField("customField")
            .getAnnotation(JsonDecimalFormat.class);
        assertThat(annotation.value()).isEqualTo("0.###");
        assertThat(annotation.roundingMode()).isEqualTo(RoundingMode.HALF_DOWN);
    }

}
