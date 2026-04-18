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

import cn.mindit.atom.core.util.MaskType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MaskTypeTest {

    @Test
    void allMaskTypesAreDefined() {
        assertThat(MaskType.values()).containsExactlyInAnyOrder(
            MaskType.CUSTOM, MaskType.NAME, MaskType.PHONE, MaskType.ID_CARD,
            MaskType.BANK_CARD, MaskType.EMAIL, MaskType.PASSWORD,
            MaskType.FIXED_PHONE, MaskType.ADDRESS, MaskType.ID,
            MaskType.CAR_LICENSE, MaskType.IPV4, MaskType.IPV6
        );
    }

    @Test
    void valueOfLooksUpEnumByName() {
        assertThat(MaskType.valueOf("PHONE")).isSameAs(MaskType.PHONE);
    }

}
