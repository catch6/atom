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

package cn.mindit.atom.test.core.util;

import cn.mindit.atom.core.util.NanoIdUtils;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class NanoIdUtilsTest {

    @Test
    void nanoIdUsesDefaultLengthFifteen() {
        assertThat(NanoIdUtils.nanoId()).hasSize(15);
    }

    @Test
    void nanoIdRespectsCustomSize() {
        assertThat(NanoIdUtils.nanoId(8)).hasSize(8);
        assertThat(NanoIdUtils.nanoId(32)).hasSize(32);
    }

    @Test
    void nanoIdCharactersAreInAlphabet() {
        String id = NanoIdUtils.nanoId(64);
        assertThat(id).matches("[0-9A-Za-z_]+");
    }

    @Test
    void nanoIdValuesAreUnique() {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            set.add(NanoIdUtils.nanoId());
        }
        assertThat(set).hasSize(1000);
    }

    @Test
    void nanoIdWithSizeZeroReturnsEmptyString() {
        assertThat(NanoIdUtils.nanoId(0)).isEmpty();
    }

}
