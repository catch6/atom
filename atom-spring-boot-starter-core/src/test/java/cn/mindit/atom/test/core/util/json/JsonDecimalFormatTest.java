/*
 * Copyright (c) 2022-2026 Catch(catchlife6@163.com).
 * Atom is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

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
