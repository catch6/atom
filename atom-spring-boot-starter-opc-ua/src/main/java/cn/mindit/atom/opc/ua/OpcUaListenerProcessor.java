/*
 * Copyright (c) 2022-2024 Catch(catchlife6@163.com).
 * Atom is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.mindit.atom.opc.ua;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.NonNull;
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
public class OpcUaListenerProcessor implements BeanPostProcessor, Ordered {

    private final List<OpcUaConsumer> consumers;

    private final Set<Class<?>> nonAnnotatedClasses = Collections.newSetFromMap(new ConcurrentHashMap<>(64));

    public OpcUaListenerProcessor() {
        consumers = new ArrayList<>();
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
        Map<Method, OpcUaListener> annotatedMethods = MethodIntrospector.selectMethods(
            targetClass,
            (MethodIntrospector.MetadataLookup<OpcUaListener>) method -> AnnotatedElementUtils.findMergedAnnotation(method, OpcUaListener.class)
        );
        if (annotatedMethods.isEmpty()) {
            return bean;
        }
        for (Map.Entry<Method, OpcUaListener> entry : annotatedMethods.entrySet()) {
            Method method = entry.getKey();
            OpcUaListener listener = entry.getValue();
            Method methodToUse = checkProxy(method, bean);
            OpcUaConsumer consumer = new OpcUaConsumer(listener.id(), listener.items(), listener.namespaceIndices(), (topic, value) -> {
                try {
                    methodToUse.invoke(bean, topic, value);
                } catch (Exception e) {
                    log.error("OPC UA invoke error", e);
                }
            });
            consumers.add(consumer);
        }
        if (log.isDebugEnabled()) {
            log.debug("{} @OpcUaListener methods processed on bean '{}': {}", annotatedMethods.size(), beanName, annotatedMethods);
        }
        return bean;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    private Method checkProxy(Method methodArg, Object bean) {
        Method method = methodArg;
        if (AopUtils.isJdkDynamicProxy(bean)) {
            try {
                // Found a @OpcUaListener method on the target class for this JDK proxy ->
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
                    "@OpcUaListener method '%s' found on bean target class '%s', " +
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
