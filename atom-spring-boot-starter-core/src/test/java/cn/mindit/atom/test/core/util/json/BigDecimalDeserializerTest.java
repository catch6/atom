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

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class BigDecimalDeserializerTest {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Bean {
        private BigDecimal amount;
    }

    @Test
    void parsesStringValue() {
        Bean bean = JsonUtils.toObject("{\"amount\":\"3.14\"}", Bean.class);
        assertThat(bean.getAmount()).isEqualByComparingTo("3.14");
    }

    @Test
    void parsesNumericValueUsingString() {
        Bean bean = JsonUtils.toObject("{\"amount\":3.14}", Bean.class);
        assertThat(bean.getAmount()).isEqualByComparingTo("3.14");
    }

    @Test
    void trimsWhitespace() {
        Bean bean = JsonUtils.toObject("{\"amount\":\"  3.14  \"}", Bean.class);
        assertThat(bean.getAmount()).isEqualByComparingTo("3.14");
    }

    @Test
    void nullValueReturnsNull() {
        Bean bean = JsonUtils.toObject("{\"amount\":null}", Bean.class);
        assertThat(bean.getAmount()).isNull();
    }

    @Test
    void emptyStringReturnsNull() {
        Bean bean = JsonUtils.toObject("{\"amount\":\"\"}", Bean.class);
        assertThat(bean.getAmount()).isNull();
    }

    @Test
    void trueAndFalseMapToOneAndZero() {
        Bean t = JsonUtils.toObject("{\"amount\":true}", Bean.class);
        Bean f = JsonUtils.toObject("{\"amount\":false}", Bean.class);
        assertThat(t.getAmount()).isEqualByComparingTo(BigDecimal.ONE);
        assertThat(f.getAmount()).isEqualByComparingTo(BigDecimal.ZERO);
    }

}
