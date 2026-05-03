package cn.mindit.atom.mqtt;

import cn.mindit.atom.core.util.AbstractListenerProcessor;
import cn.mindit.atom.core.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import java.lang.reflect.Method;

/**
 * @author Catch
 * @since 2024-06-16
 */
@Slf4j
public class MqttListenerProcessor extends AbstractListenerProcessor<MqttListener, MqttConsumer> implements BeanFactoryAware {

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    protected Class<MqttListener> annotationType() {
        return MqttListener.class;
    }

    @Override
    protected void validate(Method method) {
        Class<?>[] paramTypes = method.getParameterTypes();
        if (paramTypes.length != 2) {
            throw new IllegalArgumentException(String.format(
                "@MqttListener method '%s' in '%s' must have exactly 2 parameters: (String topic, T message)",
                method.getName(), method.getDeclaringClass().getSimpleName()));
        }
        if (paramTypes[0] != String.class) {
            throw new IllegalArgumentException(String.format(
                "@MqttListener method '%s' in '%s' first parameter must be String (topic)",
                method.getName(), method.getDeclaringClass().getSimpleName()));
        }
    }

    @Override
    protected MqttConsumer buildConsumer(Object bean, Method method, MqttListener listener) {
        Class<?> payloadType = method.getParameterTypes()[1];
        boolean needsConversion = payloadType != String.class;
        String errorHandlerBeanName = listener.errorHandler();
        return new MqttConsumer(listener.id(), listener.topics(), listener.qos(), (topic, value) -> {
            try {
                Object arg = needsConversion ? JsonUtils.toObject(value, payloadType) : value;
                method.invoke(bean, topic, arg);
            } catch (Exception e) {
                handleInvokeError(errorHandlerBeanName, topic, value, e);
            }
        });
    }

    private void handleInvokeError(String errorHandlerBeanName, String topic, String message, Exception e) {
        MqttListenerErrorHandler handler = resolveErrorHandler(errorHandlerBeanName);
        if (handler != null) {
            try {
                handler.handleError(topic, message, e);
            } catch (Exception ex) {
                log.error("MQTT error handler threw exception for topic '{}': {}", topic, ex.getMessage(), ex);
            }
        } else {
            log.error("MQTT invoke error on topic '{}'", topic, e);
        }
    }

    private MqttListenerErrorHandler resolveErrorHandler(String errorHandlerBeanName) {
        if (errorHandlerBeanName != null && !errorHandlerBeanName.isEmpty()) {
            return beanFactory.getBean(errorHandlerBeanName, MqttListenerErrorHandler.class);
        }
        try {
            return beanFactory.getBean(MqttListenerErrorHandler.class);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }

}
