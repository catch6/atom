package cn.mindit.atom.test.api.validator;

import cn.mindit.atom.api.validator.AnyOfString;
import cn.mindit.atom.api.validator.AnyOfStringValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AnyOfStringValidatorTest {

    @AnyOfString({"ACTIVE", "INACTIVE", "PENDING"})
    private String field;

    private AnyOfStringValidator validator;

    @BeforeEach
    void setUp() throws NoSuchFieldException {
        AnyOfString annotation = getClass().getDeclaredField("field").getAnnotation(AnyOfString.class);
        validator = new AnyOfStringValidator();
        validator.initialize(annotation);
    }

    @Test
    void nullIsValid() {
        assertThat(validator.isValid(null, null)).isTrue();
    }

    @Test
    void valueInAcceptsIsValid() {
        assertThat(validator.isValid("ACTIVE", null)).isTrue();
        assertThat(validator.isValid("INACTIVE", null)).isTrue();
        assertThat(validator.isValid("PENDING", null)).isTrue();
    }

    @Test
    void valueNotInAcceptsIsInvalid() {
        assertThat(validator.isValid("UNKNOWN", null)).isFalse();
        assertThat(validator.isValid("", null)).isFalse();
        assertThat(validator.isValid("active", null)).isFalse(); // 大小写敏感
    }

    @Test
    void charSequenceOtherThanStringIsAccepted() {
        assertThat(validator.isValid(new StringBuilder("ACTIVE"), null)).isTrue();
        assertThat(validator.isValid(new StringBuilder("UNKNOWN"), null)).isFalse();
    }

}
