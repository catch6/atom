package cn.mindit.atom.core.util;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Listener BeanPostProcessor 的公共骨架
 *
 * @param <A> 监听注解
 * @param <C> 生成的消费者类型
 * @author Catch
 * @since 2026-05-03
 */
@Slf4j
public abstract class AbstractListenerProcessor<A extends Annotation, C> implements BeanPostProcessor, Ordered {

    @Getter
    private final List<C> consumers = new ArrayList<>();

    private final Set<Class<?>> nonAnnotatedClasses = Collections.newSetFromMap(new ConcurrentHashMap<>(64));

    protected abstract Class<A> annotationType();

    protected abstract C buildConsumer(Object bean, Method method, A annotation);

    protected void validate(Method method) {
    }

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (nonAnnotatedClasses.contains(bean.getClass())) {
            return bean;
        }
        Class<?> targetClass = AopUtils.getTargetClass(bean);
        Class<A> annotationType = annotationType();
        Map<Method, A> annotatedMethods = MethodIntrospector.selectMethods(
            targetClass,
            (MethodIntrospector.MetadataLookup<A>) method -> AnnotatedElementUtils.findMergedAnnotation(method, annotationType)
        );
        if (annotatedMethods.isEmpty()) {
            nonAnnotatedClasses.add(bean.getClass());
            return bean;
        }
        for (Map.Entry<Method, A> entry : annotatedMethods.entrySet()) {
            Method methodToUse = checkProxy(entry.getKey(), bean);
            validate(methodToUse);
            consumers.add(buildConsumer(bean, methodToUse, entry.getValue()));
        }
        if (log.isDebugEnabled()) {
            log.debug("{} @{} methods processed on bean '{}': {}", annotatedMethods.size(), annotationType.getSimpleName(), beanName, annotatedMethods);
        }
        return bean;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    protected Method checkProxy(Method methodArg, Object bean) {
        Method method = methodArg;
        if (AopUtils.isJdkDynamicProxy(bean)) {
            try {
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
                    "@%s method '%s' found on bean target class '%s', " +
                        "but not found in any interface(s) for bean JDK proxy. Either " +
                        "pull the method up to an interface or switch to subclass (CGLIB) " +
                        "proxies by setting proxy-target-class/proxyTargetClass " +
                        "attribute to 'true'", annotationType().getSimpleName(), method.getName(),
                    method.getDeclaringClass().getSimpleName()), ex);
            }
        }
        return method;
    }

}
