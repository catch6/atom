package cn.mindit.atom.api.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Catch
 * @since 2022-10-27
 */
public class AnyOfLongValidator implements ConstraintValidator<AnyOfLong, Long> {

    private Set<Long> accepts;

    @Override
    public void initialize(AnyOfLong annotation) {
        accepts = Arrays.stream(annotation.value()).boxed().collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return value == null || accepts.contains(value);
    }

}
