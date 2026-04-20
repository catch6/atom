package cn.mindit.atom.opc.da;

import java.lang.annotation.*;

/**
 * @author Catch
 * @since 2024-06-16
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OpcDaListener {

    /**
     * 实例 ID
     */
    String id() default "";

    /**
     * 项目
     */
    String[] items();

}
