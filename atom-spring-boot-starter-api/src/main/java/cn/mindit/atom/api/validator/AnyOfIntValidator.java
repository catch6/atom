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
public class AnyOfIntValidator implements ConstraintValidator<AnyOfInt, Integer> {

    private Set<Integer> accepts;

    @Override
    public void initialize(AnyOfInt annotation) {
        accepts = Arrays.stream(annotation.value()).boxed().collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value == null || accepts.contains(value);
    }

}
