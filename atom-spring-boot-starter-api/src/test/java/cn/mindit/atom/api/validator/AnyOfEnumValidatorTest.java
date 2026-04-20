package cn.mindit.atom.api.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AnyOfEnumValidatorTest {

    enum Status {
        ACTIVE, INACTIVE, PENDING
    }

    enum OtherEnum {
        ACTIVE
    }

    @AnyOfEnum(Status.class)
    private Status field;

    private AnyOfEnumValidator validator;

    @BeforeEach
    void setUp() throws NoSuchFieldException {
        AnyOfEnum annotation = getClass().getDeclaredField("field").getAnnotation(AnyOfEnum.class);
        validator = new AnyOfEnumValidator();
        validator.initialize(annotation);
    }

    @Test
    void nullIsValid() {
        assertThat(validator.isValid(null, null)).isTrue();
    }

    @Test
    void allEnumConstantsAreValid() {
        for (Status s : Status.values()) {
            assertThat(validator.isValid(s, null)).as("Status.%s", s).isTrue();
        }
    }

    @Test
    void otherEnumWithSameNameIsInvalid() {
        // 不同枚举类型的同名常量不应被接受
        assertThat(validator.isValid(OtherEnum.ACTIVE, null)).isFalse();
    }

}
