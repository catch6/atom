package cn.mindit.atom.opc.ua;

import java.lang.annotation.*;

/**
 * @author Catch
 * @since 2024-06-16
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OpcUaListener {

    /**
     * 实例 ID, 为空则使用 OpcUaProperties.getId()
     */
    String id() default "";

    /**
     * 项目
     */
    String[] items();

    /**
     * 命名空间索引, 默认 0
     */
    int[] namespaceIndices() default {0};

}
