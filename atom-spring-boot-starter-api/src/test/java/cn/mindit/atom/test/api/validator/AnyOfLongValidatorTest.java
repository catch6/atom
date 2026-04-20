package cn.mindit.atom.test.api.validator;

import cn.mindit.atom.api.validator.AnyOfLong;
import cn.mindit.atom.api.validator.AnyOfLongValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AnyOfLongValidatorTest {

    @AnyOfLong({10L, 20L, 30L})
    private Long field;

    private AnyOfLongValidator validator;

    @BeforeEach
    void setUp() throws NoSuchFieldException {
        AnyOfLong annotation = getClass().getDeclaredField("field").getAnnotation(AnyOfLong.class);
        validator = new AnyOfLongValidator();
        validator.initialize(annotation);
    }

    @Test
    void nullIsValid() {
        assertThat(validator.isValid(null, null)).isTrue();
    }

    @Test
    void valueInAcceptsIsValid() {
        assertThat(validator.isValid(10L, null)).isTrue();
        assertThat(validator.isValid(20L, null)).isTrue();
        assertThat(validator.isValid(30L, null)).isTrue();
    }

    @Test
    void valueNotInAcceptsIsInvalid() {
        assertThat(validator.isValid(0L, null)).isFalse();
        assertThat(validator.isValid(40L, null)).isFalse();
        assertThat(validator.isValid(-1L, null)).isFalse();
        assertThat(validator.isValid(Long.MAX_VALUE, null)).isFalse();
    }

}
