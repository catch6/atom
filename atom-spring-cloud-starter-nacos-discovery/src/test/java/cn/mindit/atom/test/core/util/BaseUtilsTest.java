package cn.mindit.atom.test.core.util;

import cn.mindit.atom.core.util.BaseUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BaseUtilsTest {

    @Test
    void idToBase32ReturnsEmptyStringWhenIdIsZeroOrNegative() {
        assertThat(BaseUtils.idToBase32(0L)).isEmpty();
        assertThat(BaseUtils.idToBase32(-1L)).isEmpty();
    }

    @Test
    void idToBase32ProducesOnlyBase32Characters() {
        String encoded = BaseUtils.idToBase32(123456789L);
        assertThat(encoded).matches("[23456789A-HJ-NP-Z]+");
    }

    @Test
    void idToBase32AndBackIsReversible() {
        long id = 987654321L;
        String encoded = BaseUtils.idToBase32(id);
        assertThat(BaseUtils.base32ToId(encoded)).isEqualTo(id);
    }

    @Test
    void base32ToIdReturnsZeroForEmptyOrNull() {
        assertThat(BaseUtils.base32ToId("")).isZero();
        assertThat(BaseUtils.base32ToId(null)).isZero();
    }

    @Test
    void base32ToIdReturnsMinusOneForInvalidCharacter() {
        assertThat(BaseUtils.base32ToId("1")).isEqualTo(-1L);
        assertThat(BaseUtils.base32ToId("0")).isEqualTo(-1L);
        assertThat(BaseUtils.base32ToId("I")).isEqualTo(-1L);
    }

    @Test
    void idToBaseThrowsWhenCharactersNullOrEmpty() {
        assertThatThrownBy(() -> BaseUtils.idToBase(null, 1L))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> BaseUtils.idToBase("", 1L))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void baseToIdThrowsWhenCharactersNullOrEmpty() {
        assertThatThrownBy(() -> BaseUtils.baseToId(null, "A"))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> BaseUtils.baseToId("", "A"))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void idToBaseRoundTripWithCustomCharset() {
        String charset = "0123456789";
        long id = 42L;
        String encoded = BaseUtils.idToBase(charset, id);
        assertThat(encoded).isEqualTo("42");
        assertThat(BaseUtils.baseToId(charset, encoded)).isEqualTo(id);
    }

}
