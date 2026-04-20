package cn.mindit.atom.api.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Catch
 * @since 2022-10-27
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {AnyOfEnumValidator.class})
public @interface AnyOfEnum {

    Class<? extends Enum<?>> value();

    String message() default "must be any of enum {value}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
