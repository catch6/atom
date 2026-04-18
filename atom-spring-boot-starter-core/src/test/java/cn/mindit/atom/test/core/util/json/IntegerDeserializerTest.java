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

import static org.assertj.core.api.Assertions.assertThat;

class IntegerDeserializerTest {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Bean {
        private Integer value;
        private int primitive;
    }

    @Test
    void parsesNumericValue() {
        Bean bean = JsonUtils.toObject("{\"value\":42,\"primitive\":1}", Bean.class);
        assertThat(bean.getValue()).isEqualTo(42);
        assertThat(bean.getPrimitive()).isEqualTo(1);
    }

    @Test
    void parsesStringValue() {
        Bean bean = JsonUtils.toObject("{\"value\":\"42\",\"primitive\":\"1\"}", Bean.class);
        assertThat(bean.getValue()).isEqualTo(42);
        assertThat(bean.getPrimitive()).isEqualTo(1);
    }

    @Test
    void trimsWhitespace() {
        Bean bean = JsonUtils.toObject("{\"value\":\"  42  \",\"primitive\":0}", Bean.class);
        assertThat(bean.getValue()).isEqualTo(42);
    }

    @Test
    void nullAndEmptyReturnNullForBoxed() {
        Bean bean1 = JsonUtils.toObject("{\"value\":null,\"primitive\":0}", Bean.class);
        Bean bean2 = JsonUtils.toObject("{\"value\":\"\",\"primitive\":0}", Bean.class);
        assertThat(bean1.getValue()).isNull();
        assertThat(bean2.getValue()).isNull();
    }

    @Test
    void booleanMapsToZeroOrOne() {
        Bean bean1 = JsonUtils.toObject("{\"value\":true,\"primitive\":0}", Bean.class);
        Bean bean2 = JsonUtils.toObject("{\"value\":false,\"primitive\":0}", Bean.class);
        assertThat(bean1.getValue()).isEqualTo(1);
        assertThat(bean2.getValue()).isZero();
    }

}
