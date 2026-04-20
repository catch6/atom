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
