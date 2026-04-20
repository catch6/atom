package cn.mindit.atom.api.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class PhoneValidatorTest {

    private PhoneValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PhoneValidator();
        validator.initialize(null);
    }

    @Test
    void nullIsValid() {
        assertThat(validator.isValid(null, null)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"13800138000", "19912345678", "15666666666", "17012345678"})
    void validPhones(String phone) {
        assertThat(validator.isValid(phone, null)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",                 // 空串
            "12345678901",      // 第二位非 3-9
            "10000000000",      // 第二位 0
            "1380013800",       // 10 位
            "138001380001",     // 12 位
            "1380013800a",      // 含字母
            "23800138000",      // 不是 1 开头
            " 13800138000",     // 前置空格
    })
    void invalidPhones(String phone) {
        assertThat(validator.isValid(phone, null)).isFalse();
    }

    @Test
    void charSequenceOtherThanStringIsSupported() {
        assertThat(validator.isValid(new StringBuilder("13800138000"), null)).isTrue();
        assertThat(validator.isValid(new StringBuilder("abc"), null)).isFalse();
    }

}
