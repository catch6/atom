package cn.mindit.atom.test.api.validator;

import cn.mindit.atom.api.validator.AnyOfInt;
import cn.mindit.atom.api.validator.AnyOfIntValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AnyOfIntValidatorTest {

    @AnyOfInt({1, 2, 3})
    private Integer field;

    private AnyOfIntValidator validator;

    @BeforeEach
    void setUp() throws NoSuchFieldException {
        AnyOfInt annotation = getClass().getDeclaredField("field").getAnnotation(AnyOfInt.class);
        validator = new AnyOfIntValidator();
        validator.initialize(annotation);
    }

    @Test
    void nullIsValid() {
        assertThat(validator.isValid(null, null)).isTrue();
    }

    @Test
    void valueInAcceptsIsValid() {
        assertThat(validator.isValid(1, null)).isTrue();
        assertThat(validator.isValid(2, null)).isTrue();
        assertThat(validator.isValid(3, null)).isTrue();
    }

    @Test
    void valueNotInAcceptsIsInvalid() {
        assertThat(validator.isValid(0, null)).isFalse();
        assertThat(validator.isValid(4, null)).isFalse();
        assertThat(validator.isValid(-1, null)).isFalse();
        assertThat(validator.isValid(Integer.MAX_VALUE, null)).isFalse();
    }

}
