package cn.mindit.atom.api.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * 手机号验证器实现类
 * 实现Jakarta Validation规范的ConstraintValidator接口
 * 用于验证字符串是否符合中国大陆手机号格式
 *
 * @author Catch
 * @since 2021-04-29
 */
public class PhoneValidator implements ConstraintValidator<Phone, CharSequence> {

    private static final Pattern PATTERN = Pattern.compile("^1[3-9]\\d{9}$");

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        return value == null || PATTERN.matcher(value).matches();
    }

}
