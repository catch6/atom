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

import cn.mindit.atom.core.util.JsonUtils;
import cn.mindit.atom.core.util.json.JsonDecimalFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;

class BigDecimalSerializerTest {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class PlainBean {
        private BigDecimal amount;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class FormattedBean {
        @JsonDecimalFormat("0.00")
        private BigDecimal amount;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class HalfDownBean {
        @JsonDecimalFormat(value = "0.00", roundingMode = RoundingMode.HALF_DOWN)
        private BigDecimal amount;
    }

    @Test
    void withoutAnnotationUsesPlainString() {
        String json = JsonUtils.toJson(new PlainBean(new BigDecimal("3.14159")));
        assertThat(json).contains("\"amount\":\"3.14159\"");
    }

    @Test
    void withFormatAnnotationRoundsHalfUp() {
        String json = JsonUtils.toJson(new FormattedBean(new BigDecimal("3.145")));
        assertThat(json).contains("\"amount\":\"3.15\"");
    }

    @Test
    void withFormatAnnotationPadsDecimalPlaces() {
        String json = JsonUtils.toJson(new FormattedBean(new BigDecimal("3")));
        assertThat(json).contains("\"amount\":\"3.00\"");
    }

    @Test
    void customRoundingModeIsApplied() {
        String json = JsonUtils.toJson(new HalfDownBean(new BigDecimal("3.145")));
        assertThat(json).contains("\"amount\":\"3.14\"");
    }

}
