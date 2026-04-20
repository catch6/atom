package cn.mindit.atom.core.util.json;

import cn.mindit.atom.core.util.MaskType;
import com.fasterxml.jackson.annotation.JacksonAnnotation;
import tools.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Catch
 * @since 2023-08-25
 */
@JacksonAnnotation
@JsonSerialize(using = MaskSerializer.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
public @interface JsonMask {

    /**
     * 脱敏数据类型，在 CUSTOM 的时, start 和 end 生效
     */
    MaskType value() default MaskType.CUSTOM;

    /**
     * 脱敏开始位置（包含）
     */
    int start() default 0;

    /**
     * 脱敏结束位置（不包含）
     */
    int end() default 0;

}
