package cn.mindit.atom.mqtt;

import java.lang.annotation.*;

/**
 * @author Catch
 * @since 2024-06-25
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MqttListener {

    /**
     * 实例 ID, 为空则使用 MqttProperties.getId()
     *
     * @return 实例 ID
     */
    String id() default "default";

    /**
     * 订阅主题
     *
     * @return 主题
     */
    String[] topics();

    /**
     * QoS
     *
     * @return QoS, 默认为 0
     */
    int[] qos() default 0;

    /**
     * 异常处理器 Bean 名称, 为空则使用全局 {@link MqttListenerErrorHandler} Bean
     *
     * @return 异常处理器 Bean 名称
     */
    String errorHandler() default "";

}
