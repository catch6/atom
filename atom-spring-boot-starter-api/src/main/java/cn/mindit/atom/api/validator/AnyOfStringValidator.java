package cn.mindit.atom.api.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Catch
 * @since 2022-10-27
 */
public class AnyOfStringValidator implements ConstraintValidator<AnyOfString, CharSequence> {

    private Set<String> accepts = new HashSet<>();

    @Override
    public void initialize(AnyOfString annotation) {
        accepts = Arrays.stream(annotation.value()).collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        return value == null || accepts.contains(value.toString());
    }

}
