package cn.mindit.atom.core.util;

import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 解析字符串数组中的 SpEL / 占位符表达式
 *
 * @author Catch
 * @since 2026-05-03
 */
public abstract class ExpressionResolverUtils {

    public static String[] resolveStringArray(String[] values, ConfigurableListableBeanFactory beanFactory, String label) {
        if (values == null || values.length == 0) {
            return null;
        }
        BeanExpressionResolver resolver = beanFactory.getBeanExpressionResolver();
        if (resolver == null) {
            return values;
        }
        BeanExpressionContext context = new BeanExpressionContext(beanFactory, null);
        List<String> result = new ArrayList<>();
        for (String value : values) {
            Object object = resolver.evaluate(beanFactory.resolveEmbeddedValue(value), context);
            if (object == null) {
                throw new IllegalArgumentException(label + " must not be null");
            }
            if (object instanceof String str) {
                result.add(str);
            } else if (object instanceof String[] strs) {
                for (String str : strs) {
                    if (str == null) {
                        throw new IllegalArgumentException(label + " must not be null");
                    }
                    result.add(str);
                }
            } else {
                throw new IllegalArgumentException(label + " must be String or String[]");
            }
        }
        return result.toArray(new String[0]);
    }

}
