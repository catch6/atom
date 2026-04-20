package cn.mindit.atom.api.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Catch
 * @since 2022-10-27
 */
public class AnyOfEnumValidator implements ConstraintValidator<AnyOfEnum, Enum<?>> {

    private Set<Enum<?>> accepts;

    @Override
    public void initialize(AnyOfEnum annotation) {
        Enum<?>[] enums = annotation.value().getEnumConstants();
        accepts = Stream.of(enums).collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
        return value == null || accepts.contains(value);
    }

}
