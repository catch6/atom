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
