/*
 * Copyright (c) 2022-2023 Catch
 * [Atom] is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package net.wenzuo.atom.core.utils;

import net.wenzuo.atom.core.exception.ServerException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * <p>服务数据校验断言，此断言抛出 ServerException, 报 error 日志，异常应该报警</p>
 * <p>如：手机号发送短信失败，服务链路调用失败</p>
 *
 * @author Catch
 * @since 2021-12-25
 */
public abstract class Must {

    public static void isEquals(Object a, Object b, String message) {
        if (!Objects.equals(a, b)) {
            throw new ServerException(message);
        }
    }

    public static void isEquals(Object a, Object b, Supplier<String> messageSupplier) {
        if (!Objects.equals(a, b)) {
            throw new ServerException(nullSafeGet(messageSupplier));
        }
    }

    public static void notEquals(Object a, Object b, String message) {
        if (Objects.equals(a, b)) {
            throw new ServerException(message);
        }
    }

    public static void notEquals(Object a, Object b, Supplier<String> messageSupplier) {
        if (Objects.equals(a, b)) {
            throw new ServerException(nullSafeGet(messageSupplier));
        }
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new ServerException(message);
        }
    }

    public static void isTrue(boolean expression, Supplier<String> messageSupplier) {
        if (!expression) {
            throw new ServerException(nullSafeGet(messageSupplier));
        }
    }

    public static void isFalse(boolean expression, String message) {
        if (expression) {
            throw new ServerException(message);
        }
    }

    public static void isFalse(boolean expression, Supplier<String> messageSupplier) {
        if (expression) {
            throw new ServerException(nullSafeGet(messageSupplier));
        }
    }

    public static void isNull(@Nullable Object object, String message) {
        if (object != null) {
            throw new ServerException(message);
        }
    }

    public static void isNull(@Nullable Object object, Supplier<String> messageSupplier) {
        if (object != null) {
            throw new ServerException(nullSafeGet(messageSupplier));
        }
    }

    public static void notNull(@Nullable Object object, String message) {
        if (object == null) {
            throw new ServerException(message);
        }
    }

    public static void notNull(@Nullable Object object, Supplier<String> messageSupplier) {
        if (object == null) {
            throw new ServerException(nullSafeGet(messageSupplier));
        }
    }

    public static void isEmpty(@Nullable String text, String message) {
        if (text != null && text.length() > 0) {
            throw new ServerException(message);
        }
    }

    public static void isEmpty(@Nullable String text, Supplier<String> messageSupplier) {
        if (text != null && text.length() > 0) {
            throw new ServerException(nullSafeGet(messageSupplier));
        }
    }

    public static void notEmpty(@Nullable String text, String message) {
        if (text == null || text.length() == 0) {
            throw new ServerException(message);
        }
    }

    public static void notEmpty(@Nullable String text, Supplier<String> messageSupplier) {
        if (text == null || text.length() == 0) {
            throw new ServerException(nullSafeGet(messageSupplier));
        }
    }

    public static void isBlank(@Nullable String text, String message) {
        if (StringUtils.hasText(text)) {
            throw new ServerException(message);
        }
    }

    public static void isBlank(@Nullable String text, Supplier<String> messageSupplier) {
        if (StringUtils.hasText(text)) {
            throw new ServerException(nullSafeGet(messageSupplier));
        }
    }

    public static void notBlank(@Nullable String text, String message) {
        if (!StringUtils.hasText(text)) {
            throw new ServerException(message);
        }
    }

    public static void notBlank(@Nullable String text, Supplier<String> messageSupplier) {
        if (!StringUtils.hasText(text)) {
            throw new ServerException(nullSafeGet(messageSupplier));
        }
    }

    public static void isContains(@Nullable String textToSearch, String substring, String message) {
        if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) && !textToSearch.contains(substring)) {
            throw new ServerException(message);
        }
    }

    public static void isContains(@Nullable String textToSearch, String substring, Supplier<String> messageSupplier) {
        if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) && !textToSearch.contains(substring)) {
            throw new ServerException(nullSafeGet(messageSupplier));
        }
    }

    public static void notContains(@Nullable String textToSearch, String substring, String message) {
        if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) && textToSearch.contains(substring)) {
            throw new ServerException(message);
        }
    }

    public static void notContains(@Nullable String textToSearch, String substring, Supplier<String> messageSupplier) {
        if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) && textToSearch.contains(substring)) {
            throw new ServerException(nullSafeGet(messageSupplier));
        }
    }

    public static void isEmpty(@Nullable Object[] array, String message) {
        if (array != null && array.length > 0) {
            throw new ServerException(message);
        }
    }

    public static void isEmpty(@Nullable Object[] array, Supplier<String> messageSupplier) {
        if (array != null && array.length > 0) {
            throw new ServerException(nullSafeGet(messageSupplier));
        }
    }

    public static void notEmpty(@Nullable Object[] array, String message) {
        if (array == null || array.length == 0) {
            throw new ServerException(message);
        }
    }

    public static void notEmpty(@Nullable Object[] array, Supplier<String> messageSupplier) {
        if (array == null || array.length == 0) {
            throw new ServerException(nullSafeGet(messageSupplier));
        }
    }

    public static void noNullElements(@Nullable Object[] array, String message) {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    throw new ServerException(message);
                }
            }
        }
    }

    public static void noNullElements(@Nullable Object[] array, Supplier<String> messageSupplier) {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    throw new ServerException(nullSafeGet(messageSupplier));
                }
            }
        }
    }

    public static void isEmpty(@Nullable Collection<?> collection, String message) {
        if (!CollectionUtils.isEmpty(collection)) {
            throw new ServerException(message);
        }
    }

    public static void isEmpty(@Nullable Collection<?> collection, Supplier<String> messageSupplier) {
        if (!CollectionUtils.isEmpty(collection)) {
            throw new ServerException(nullSafeGet(messageSupplier));
        }
    }

    public static void notEmpty(@Nullable Collection<?> collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new ServerException(message);
        }
    }

    public static void notEmpty(@Nullable Collection<?> collection, Supplier<String> messageSupplier) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new ServerException(nullSafeGet(messageSupplier));
        }
    }

    public static void noNullElements(@Nullable Collection<?> collection, String message) {
        if (collection != null) {
            for (Object element : collection) {
                if (element == null) {
                    throw new ServerException(message);
                }
            }
        }
    }

    public static void noNullElements(@Nullable Collection<?> collection, Supplier<String> messageSupplier) {
        if (collection != null) {
            for (Object element : collection) {
                if (element == null) {
                    throw new ServerException(nullSafeGet(messageSupplier));
                }
            }
        }
    }

    public static void isEmpty(@Nullable Map<?, ?> map, String message) {
        if (!CollectionUtils.isEmpty(map)) {
            throw new ServerException(message);
        }
    }

    public static void isEmpty(@Nullable Map<?, ?> map, Supplier<String> messageSupplier) {
        if (!CollectionUtils.isEmpty(map)) {
            throw new ServerException(nullSafeGet(messageSupplier));
        }
    }

    public static void notEmpty(@Nullable Map<?, ?> map, String message) {
        if (CollectionUtils.isEmpty(map)) {
            throw new ServerException(message);
        }
    }

    public static void notEmpty(@Nullable Map<?, ?> map, Supplier<String> messageSupplier) {
        if (CollectionUtils.isEmpty(map)) {
            throw new ServerException(nullSafeGet(messageSupplier));
        }
    }

    public static void isInstanceOf(@NonNull Class<?> type, @Nullable Object obj, String message) {
        if (!type.isInstance(obj)) {
            instanceCheckFailed(type, obj, message);
        }
    }

    public static void isInstanceOf(@NonNull Class<?> type, @Nullable Object obj, Supplier<String> messageSupplier) {
        if (!type.isInstance(obj)) {
            instanceCheckFailed(type, obj, nullSafeGet(messageSupplier));
        }
    }

    public static void notInstanceOf(@NonNull Class<?> type, @Nullable Object obj, String message) {
        if (type.isInstance(obj)) {
            instanceCheckFailed(type, obj, message);
        }
    }

    public static void notInstanceOf(@NonNull Class<?> type, @Nullable Object obj, Supplier<String> messageSupplier) {
        if (type.isInstance(obj)) {
            instanceCheckFailed(type, obj, nullSafeGet(messageSupplier));
        }
    }

    public static void isAssignable(@NonNull Class<?> superType, @Nullable Class<?> subType, String message) {
        if (subType == null || !superType.isAssignableFrom(subType)) {
            assignableCheckFailed(superType, subType, message);
        }
    }

    public static void isAssignable(@NonNull Class<?> superType, @Nullable Class<?> subType, Supplier<String> messageSupplier) {
        if (subType == null || !superType.isAssignableFrom(subType)) {
            assignableCheckFailed(superType, subType, nullSafeGet(messageSupplier));
        }
    }

    private static void instanceCheckFailed(Class<?> type, @Nullable Object obj, @Nullable String message) {
        String className = (obj != null ? obj.getClass()
                                             .getName() : "null");
        String result = "";
        boolean defaultMessage = true;
        if (StringUtils.hasLength(message)) {
            if (endsWithSeparator(message)) {
                result = message + " ";
            } else {
                result = messageWithTypeName(message, className);
                defaultMessage = false;
            }
        }
        if (defaultMessage) {
            result = result + ("Object of class [" + className + "] must be an instance of " + type);
        }
        throw new ServerException(result);
    }

    private static void assignableCheckFailed(Class<?> superType, @Nullable Class<?> subType, @Nullable String message) {
        String result = "";
        boolean defaultMessage = true;
        if (StringUtils.hasLength(message)) {
            if (endsWithSeparator(message)) {
                result = message + " ";
            } else {
                result = messageWithTypeName(message, subType);
                defaultMessage = false;
            }
        }
        if (defaultMessage) {
            result = result + (subType + " is not assignable to " + superType);
        }
        throw new ServerException(result);
    }

    private static boolean endsWithSeparator(String message) {
        return (message.endsWith(":") || message.endsWith(";") || message.endsWith(",") || message.endsWith("."));
    }

    private static String messageWithTypeName(String message, @Nullable Object typeName) {
        return message + (message.endsWith(" ") ? "" : ": ") + typeName;
    }

    @Nullable
    private static String nullSafeGet(@Nullable Supplier<String> messageSupplier) {
        return (messageSupplier != null ? messageSupplier.get() : null);
    }

}
