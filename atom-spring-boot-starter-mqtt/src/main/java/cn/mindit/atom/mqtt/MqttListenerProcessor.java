package cn.mindit.atom.mqtt;

import cn.mindit.atom.core.util.JsonUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Catch
 * @since 2024-06-16
 */
@Slf4j
@Getter
public class MqttListenerProcessor implements BeanPostProcessor, Ordered, BeanFactoryAware {

    private final List<MqttConsumer> consumers;

    private final Set<Class<?>> nonAnnotatedClasses = Collections.newSetFromMap(new ConcurrentHashMap<>(64));

    private BeanFactory beanFactory;

    public MqttListenerProcessor() {
        consumers = new ArrayList<>();
    }

    @Override
    public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (nonAnnotatedClasses.contains(bean.getClass())) {
            return bean;
        }
        Class<?> targetClass = AopUtils.getTargetClass(bean);
        Map<Method, MqttListener> annotatedMethods = MethodIntrospector.selectMethods(
            targetClass,
            (MethodIntrospector.MetadataLookup<MqttListener>) method -> AnnotatedElementUtils.findMergedAnnotation(method, MqttListener.class)
        );
        if (annotatedMethods.isEmpty()) {
            return bean;
        }
        for (Map.Entry<Method, MqttListener> entry : annotatedMethods.entrySet()) {
            Method method = entry.getKey();
            MqttListener listener = entry.getValue();
            Method methodToUse = checkProxy(method, bean);
            validateMethodSignature(methodToUse);
            Class<?> payloadType = methodToUse.getParameterTypes()[1];
            boolean needsConversion = payloadType != String.class;
            String errorHandlerBeanName = listener.errorHandler();
            MqttConsumer consumer = new MqttConsumer(listener.id(), listener.topics(), listener.qos(), (topic, value) -> {
                try {
                    Object arg = needsConversion ? JsonUtils.toObject(value, payloadType) : value;
                    methodToUse.invoke(bean, topic, arg);
                } catch (Exception e) {
                    handleInvokeError(errorHandlerBeanName, topic, value, e);
                }
            });
            consumers.add(consumer);
        }
        if (log.isDebugEnabled()) {
            log.debug("{} @MqttListener methods processed on bean '{}': {}", annotatedMethods.size(), beanName, annotatedMethods);
        }
        return bean;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    private void validateMethodSignature(Method method) {
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

    private Method checkProxy(Method methodArg, Object bean) {
        Method method = methodArg;
        if (AopUtils.isJdkDynamicProxy(bean)) {
            try {
                // Found a @MqttListener method on the target class for this JDK proxy ->
                // is it also present on the proxy itself?
                method = bean.getClass().getMethod(method.getName(), method.getParameterTypes());
                Class<?>[] proxiedInterfaces = ((Advised) bean).getProxiedInterfaces();
                for (Class<?> iface : proxiedInterfaces) {
                    try {
                        method = iface.getMethod(method.getName(), method.getParameterTypes());
                        break;
                    } catch (@SuppressWarnings("unused") NoSuchMethodException noMethod) {
                        // NOSONAR
                    }
                }
            } catch (SecurityException ex) {
                ReflectionUtils.handleReflectionException(ex);
            } catch (NoSuchMethodException ex) {
                throw new IllegalStateException(String.format(
                    "@MqttListener method '%s' found on bean target class '%s', " +
                        "but not found in any interface(s) for bean JDK proxy. Either " +
                        "pull the method up to an interface or switch to subclass (CGLIB) " +
                        "proxies by setting proxy-target-class/proxyTargetClass " +
                        "attribute to 'true'", method.getName(),
                    method.getDeclaringClass().getSimpleName()), ex);
            }
        }
        return method;
    }

}
