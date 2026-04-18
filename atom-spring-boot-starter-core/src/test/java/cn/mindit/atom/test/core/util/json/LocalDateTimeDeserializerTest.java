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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

class LocalDateTimeDeserializerTest {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Bean {
        private LocalDateTime value;
    }

    @Test
    void parsesFormattedString() {
        Bean bean = JsonUtils.toObject("{\"value\":\"2024-06-01 12:30:45\"}", Bean.class);
        assertThat(bean.getValue()).isEqualTo(LocalDateTime.of(2024, 6, 1, 12, 30, 45));
    }

    @Test
    void parsesEpochMillis() {
        long epochMillis = 1_700_000_000_000L;
        Bean bean = JsonUtils.toObject("{\"value\":" + epochMillis + "}", Bean.class);
        LocalDateTime expected = LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMillis), ZoneId.systemDefault());
        assertThat(bean.getValue()).isEqualTo(expected);
    }

    @Test
    void parsesEpochSeconds() {
        long epochSeconds = 1_700_000_000L;
        Bean bean = JsonUtils.toObject("{\"value\":" + epochSeconds + "}", Bean.class);
        LocalDateTime expected = LocalDateTime.ofInstant(Instant.ofEpochSecond(epochSeconds), ZoneId.systemDefault());
        assertThat(bean.getValue()).isEqualTo(expected);
    }

    @Test
    void parsesArrayForm() {
        Bean bean = JsonUtils.toObject("{\"value\":[2024,6,1,12,30,45]}", Bean.class);
        assertThat(bean.getValue()).isEqualTo(LocalDateTime.of(2024, 6, 1, 12, 30, 45));
    }

    @Test
    void parsesArrayFormWithoutSeconds() {
        Bean bean = JsonUtils.toObject("{\"value\":[2024,6,1,12,30]}", Bean.class);
        assertThat(bean.getValue()).isEqualTo(LocalDateTime.of(2024, 6, 1, 12, 30));
    }

    @Test
    void parsesEmptyArrayAsNull() {
        Bean bean = JsonUtils.toObject("{\"value\":[]}", Bean.class);
        assertThat(bean.getValue()).isNull();
    }

}
