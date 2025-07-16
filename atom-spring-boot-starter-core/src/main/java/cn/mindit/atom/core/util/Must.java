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

package cn.mindit.atom.core.util;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * <p>服务数据校验断言，此断言抛出 ServiceException, 报 error 日志，异常应该报警</p>
 * <p>如：手机号发送短信失败，服务链路调用失败</p>
 *
 * @author Catch
 * @since 2021-12-25
 */
public abstract class Must {

    public static void isEquals(Object a, Object b, String message) {
        if (!Objects.equals(a, b)) {
            throw new ServiceException(message);
        }
    }

    public static void isEquals(Object a, Object b, int code, String message) {
        if (!Objects.equals(a, b)) {
            throw new ServiceException(code, message);
        }
    }

    public static void isEquals(Object a, Object b, ResultProvider provider) {
        if (!Objects.equals(a, b)) {
            throw new ServiceException(provider);
        }
    }

    public static void isEquals(Object a, Object b, Supplier<String> messageSupplier) {
        if (!Objects.equals(a, b)) {
            throw new ServiceException(nullSafeGet(messageSupplier));
        }
    }

    public static void isEquals(Object a, Object b, int code, Supplier<String> messageSupplier) {
        if (!Objects.equals(a, b)) {
            throw new ServiceException(code, nullSafeGet(messageSupplier));
        }
    }

    public static void notEquals(Object a, Object b, String message) {
        if (Objects.equals(a, b)) {
            throw new ServiceException(message);
        }
    }

    public static void notEquals(Object a, Object b, int code, String message) {
        if (Objects.equals(a, b)) {
            throw new ServiceException(code, message);
        }
    }

    public static void notEquals(Object a, Object b, ResultProvider provider) {
        if (Objects.equals(a, b)) {
            throw new ServiceException(provider);
        }
    }

    public static void notEquals(Object a, Object b, Supplier<String> messageSupplier) {
        if (Objects.equals(a, b)) {
            throw new ServiceException(nullSafeGet(messageSupplier));
        }
    }

    public static void notEquals(Object a, Object b, int code, Supplier<String> messageSupplier) {
        if (Objects.equals(a, b)) {
            throw new ServiceException(code, nullSafeGet(messageSupplier));
        }
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new ServiceException(message);
        }
    }

    public static void isTrue(boolean expression, int code, String message) {
        if (!expression) {
            throw new ServiceException(code, message);
        }
    }

    public static void isTrue(boolean expression, ResultProvider provider) {
        if (!expression) {
            throw new ServiceException(provider);
        }
    }

    public static void isTrue(boolean expression, Supplier<String> messageSupplier) {
        if (!expression) {
            throw new ServiceException(nullSafeGet(messageSupplier));
        }
    }

    public static void isTrue(boolean expression, int code, Supplier<String> messageSupplier) {
        if (!expression) {
            throw new ServiceException(code, nullSafeGet(messageSupplier));
        }
    }

    public static void isFalse(boolean expression, String message) {
        if (expression) {
            throw new ServiceException(message);
        }
    }

    public static void isFalse(boolean expression, int code, String message) {
        if (expression) {
            throw new ServiceException(code, message);
        }
    }

    public static void isFalse(boolean expression, ResultProvider provider) {
        if (expression) {
            throw new ServiceException(provider);
        }
    }

    public static void isFalse(boolean expression, Supplier<String> messageSupplier) {
        if (expression) {
            throw new ServiceException(nullSafeGet(messageSupplier));
        }
    }

    public static void isFalse(boolean expression, int code, Supplier<String> messageSupplier) {
        if (expression) {
            throw new ServiceException(code, nullSafeGet(messageSupplier));
        }
    }

    public static void isNull(@Nullable Object object, String message) {
        if (object != null) {
            throw new ServiceException(message);
        }
    }

    public static void isNull(@Nullable Object object, int code, String message) {
        if (object != null) {
            throw new ServiceException(code, message);
        }
    }

    public static void isNull(@Nullable Object object, ResultProvider provider) {
        if (object != null) {
            throw new ServiceException(provider);
        }
    }

    public static void isNull(@Nullable Object object, Supplier<String> messageSupplier) {
        if (object != null) {
            throw new ServiceException(nullSafeGet(messageSupplier));
        }
    }

    public static void isNull(@Nullable Object object, int code, Supplier<String> messageSupplier) {
        if (object != null) {
            throw new ServiceException(code, nullSafeGet(messageSupplier));
        }
    }

    public static void notNull(@Nullable Object object, String message) {
        if (object == null) {
            throw new ServiceException(message);
        }
    }

    public static void notNull(@Nullable Object object, int code, String message) {
        if (object == null) {
            throw new ServiceException(code, message);
        }
    }

    public static void notNull(@Nullable Object object, ResultProvider provider) {
        if (object == null) {
            throw new ServiceException(provider);
        }
    }

    public static void notNull(@Nullable Object object, Supplier<String> messageSupplier) {
        if (object == null) {
            throw new ServiceException(nullSafeGet(messageSupplier));
        }
    }

    public static void notNull(@Nullable Object object, int code, Supplier<String> messageSupplier) {
        if (object == null) {
            throw new ServiceException(code, nullSafeGet(messageSupplier));
        }
    }

    public static void isEmpty(@Nullable String text, String message) {
        if (text != null && text.length() > 0) {
            throw new ServiceException(message);
        }
    }

    public static void isEmpty(@Nullable String text, int code, String message) {
        if (text != null && text.length() > 0) {
            throw new ServiceException(code, message);
        }
    }

    public static void isEmpty(@Nullable String text, ResultProvider provider) {
        if (text != null && text.length() > 0) {
            throw new ServiceException(provider);
        }
    }

    public static void isEmpty(@Nullable String text, Supplier<String> messageSupplier) {
        if (text != null && text.length() > 0) {
            throw new ServiceException(nullSafeGet(messageSupplier));
        }
    }

    public static void isEmpty(@Nullable String text, int code, Supplier<String> messageSupplier) {
        if (text != null && text.length() > 0) {
            throw new ServiceException(code, nullSafeGet(messageSupplier));
        }
    }

    public static void notEmpty(@Nullable String text, String message) {
        if (text == null || text.length() == 0) {
            throw new ServiceException(message);
        }
    }

    public static void notEmpty(@Nullable String text, int code, String message) {
        if (text == null || text.length() == 0) {
            throw new ServiceException(code, message);
        }
    }

    public static void notEmpty(@Nullable String text, ResultProvider provider) {
        if (text == null || text.length() == 0) {
            throw new ServiceException(provider);
        }
    }

    public static void notEmpty(@Nullable String text, Supplier<String> messageSupplier) {
        if (text == null || text.length() == 0) {
            throw new ServiceException(nullSafeGet(messageSupplier));
        }
    }

    public static void notEmpty(@Nullable String text, int code, Supplier<String> messageSupplier) {
        if (text == null || text.length() == 0) {
            throw new ServiceException(code, nullSafeGet(messageSupplier));
        }
    }

    public static void isBlank(@Nullable String text, String message) {
        if (StringUtils.hasText(text)) {
            throw new ServiceException(message);
        }
    }

    public static void isBlank(@Nullable String text, int code, String message) {
        if (StringUtils.hasText(text)) {
            throw new ServiceException(code, message);
        }
    }

    public static void isBlank(@Nullable String text, ResultProvider provider) {
        if (StringUtils.hasText(text)) {
            throw new ServiceException(provider);
        }
    }

    public static void isBlank(@Nullable String text, Supplier<String> messageSupplier) {
        if (StringUtils.hasText(text)) {
            throw new ServiceException(nullSafeGet(messageSupplier));
        }
    }

    public static void isBlank(@Nullable String text, int code, Supplier<String> messageSupplier) {
        if (StringUtils.hasText(text)) {
            throw new ServiceException(code, nullSafeGet(messageSupplier));
        }
    }

    public static void notBlank(@Nullable String text, String message) {
        if (!StringUtils.hasText(text)) {
            throw new ServiceException(message);
        }
    }

    public static void notBlank(@Nullable String text, int code, String message) {
        if (!StringUtils.hasText(text)) {
            throw new ServiceException(code, message);
        }
    }

    public static void notBlank(@Nullable String text, ResultProvider provider) {
        if (!StringUtils.hasText(text)) {
            throw new ServiceException(provider);
        }
    }

    public static void notBlank(@Nullable String text, Supplier<String> messageSupplier) {
        if (!StringUtils.hasText(text)) {
            throw new ServiceException(nullSafeGet(messageSupplier));
        }
    }

    public static void notBlank(@Nullable String text, int code, Supplier<String> messageSupplier) {
        if (!StringUtils.hasText(text)) {
            throw new ServiceException(code, nullSafeGet(messageSupplier));
        }
    }

    public static void isContains(@Nullable String textToSearch, String substring, String message) {
        if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) && !textToSearch.contains(substring)) {
            throw new ServiceException(message);
        }
    }

    public static void isContains(@Nullable String textToSearch, String substring, int code, String message) {
        if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) && !textToSearch.contains(substring)) {
            throw new ServiceException(code, message);
        }
    }

    public static void isContains(@Nullable String textToSearch, String substring, ResultProvider provider) {
        if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) && !textToSearch.contains(substring)) {
            throw new ServiceException(provider);
        }
    }

    public static void isContains(@Nullable String textToSearch, String substring, Supplier<String> messageSupplier) {
        if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) && !textToSearch.contains(substring)) {
            throw new ServiceException(nullSafeGet(messageSupplier));
        }
    }

    public static void isContains(@Nullable String textToSearch, String substring, int code, Supplier<String> messageSupplier) {
        if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) && !textToSearch.contains(substring)) {
            throw new ServiceException(code, nullSafeGet(messageSupplier));
        }
    }

    public static void notContains(@Nullable String textToSearch, String substring, String message) {
        if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) && textToSearch.contains(substring)) {
            throw new ServiceException(message);
        }
    }

    public static void notContains(@Nullable String textToSearch, String substring, int code, String message) {
        if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) && textToSearch.contains(substring)) {
            throw new ServiceException(code, message);
        }
    }

    public static void notContains(@Nullable String textToSearch, String substring, ResultProvider provider) {
        if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) && textToSearch.contains(substring)) {
            throw new ServiceException(provider);
        }
    }

    public static void notContains(@Nullable String textToSearch, String substring, Supplier<String> messageSupplier) {
        if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) && textToSearch.contains(substring)) {
            throw new ServiceException(nullSafeGet(messageSupplier));
        }
    }

    public static void notContains(@Nullable String textToSearch, String substring, int code, Supplier<String> messageSupplier) {
        if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) && textToSearch.contains(substring)) {
            throw new ServiceException(code, nullSafeGet(messageSupplier));
        }
    }

    public static void isEmpty(@Nullable Object[] array, String message) {
        if (array != null && array.length > 0) {
            throw new ServiceException(message);
        }
    }

    public static void isEmpty(@Nullable Object[] array, int code, String message) {
        if (array != null && array.length > 0) {
            throw new ServiceException(code, message);
        }
    }

    public static void isEmpty(@Nullable Object[] array, ResultProvider provider) {
        if (array != null && array.length > 0) {
            throw new ServiceException(provider);
        }
    }

    public static void isEmpty(@Nullable Object[] array, Supplier<String> messageSupplier) {
        if (array != null && array.length > 0) {
            throw new ServiceException(nullSafeGet(messageSupplier));
        }
    }

    public static void isEmpty(@Nullable Object[] array, int code, Supplier<String> messageSupplier) {
        if (array != null && array.length > 0) {
            throw new ServiceException(code, nullSafeGet(messageSupplier));
        }
    }

    public static void notEmpty(@Nullable Object[] array, String message) {
        if (array == null || array.length == 0) {
            throw new ServiceException(message);
        }
    }

    public static void notEmpty(@Nullable Object[] array, int code, String message) {
        if (array == null || array.length == 0) {
            throw new ServiceException(code, message);
        }
    }

    public static void notEmpty(@Nullable Object[] array, ResultProvider provider) {
        if (array == null || array.length == 0) {
            throw new ServiceException(provider);
        }
    }

    public static void notEmpty(@Nullable Object[] array, Supplier<String> messageSupplier) {
        if (array == null || array.length == 0) {
            throw new ServiceException(nullSafeGet(messageSupplier));
        }
    }

    public static void notEmpty(@Nullable Object[] array, int code, Supplier<String> messageSupplier) {
        if (array == null || array.length == 0) {
            throw new ServiceException(code, nullSafeGet(messageSupplier));
        }
    }

    public static void noNullElements(@Nullable Object[] array, String message) {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    throw new ServiceException(message);
                }
            }
        }
    }

    public static void noNullElements(@Nullable Object[] array, int code, String message) {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    throw new ServiceException(code, message);
                }
            }
        }
    }

    public static void noNullElements(@Nullable Object[] array, ResultProvider provider) {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    throw new ServiceException(provider);
                }
            }
        }
    }

    public static void noNullElements(@Nullable Object[] array, Supplier<String> messageSupplier) {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    throw new ServiceException(nullSafeGet(messageSupplier));
                }
            }
        }
    }

    public static void noNullElements(@Nullable Object[] array, int code, Supplier<String> messageSupplier) {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    throw new ServiceException(code, nullSafeGet(messageSupplier));
                }
            }
        }
    }

    public static void isEmpty(@Nullable Collection<?> collection, String message) {
        if (!CollectionUtils.isEmpty(collection)) {
            throw new ServiceException(message);
        }
    }

    public static void isEmpty(@Nullable Collection<?> collection, int code, String message) {
        if (!CollectionUtils.isEmpty(collection)) {
            throw new ServiceException(code, message);
        }
    }

    public static void isEmpty(@Nullable Collection<?> collection, ResultProvider provider) {
        if (!CollectionUtils.isEmpty(collection)) {
            throw new ServiceException(provider);
        }
    }

    public static void isEmpty(@Nullable Collection<?> collection, Supplier<String> messageSupplier) {
        if (!CollectionUtils.isEmpty(collection)) {
            throw new ServiceException(nullSafeGet(messageSupplier));
        }
    }

    public static void isEmpty(@Nullable Collection<?> collection, int code, Supplier<String> messageSupplier) {
        if (!CollectionUtils.isEmpty(collection)) {
            throw new ServiceException(code, nullSafeGet(messageSupplier));
        }
    }

    public static void notEmpty(@Nullable Collection<?> collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new ServiceException(message);
        }
    }

    public static void notEmpty(@Nullable Collection<?> collection, int code, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new ServiceException(code, message);
        }
    }

    public static void notEmpty(@Nullable Collection<?> collection, ResultProvider provider) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new ServiceException(provider);
        }
    }

    public static void notEmpty(@Nullable Collection<?> collection, Supplier<String> messageSupplier) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new ServiceException(nullSafeGet(messageSupplier));
        }
    }

    public static void notEmpty(@Nullable Collection<?> collection, int code, Supplier<String> messageSupplier) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new ServiceException(code, nullSafeGet(messageSupplier));
        }
    }

    public static void noNullElements(@Nullable Collection<?> collection, String message) {
        if (collection != null) {
            for (Object element : collection) {
                if (element == null) {
                    throw new ServiceException(message);
                }
            }
        }
    }

    public static void noNullElements(@Nullable Collection<?> collection, int code, String message) {
        if (collection != null) {
            for (Object element : collection) {
                if (element == null) {
                    throw new ServiceException(code, message);
                }
            }
        }
    }

    public static void noNullElements(@Nullable Collection<?> collection, ResultProvider provider) {
        if (collection != null) {
            for (Object element : collection) {
                if (element == null) {
                    throw new ServiceException(provider);
                }
            }
        }
    }

    public static void noNullElements(@Nullable Collection<?> collection, Supplier<String> messageSupplier) {
        if (collection != null) {
            for (Object element : collection) {
                if (element == null) {
                    throw new ServiceException(nullSafeGet(messageSupplier));
                }
            }
        }
    }

    public static void noNullElements(@Nullable Collection<?> collection, int code, Supplier<String> messageSupplier) {
        if (collection != null) {
            for (Object element : collection) {
                if (element == null) {
                    throw new ServiceException(code, nullSafeGet(messageSupplier));
                }
            }
        }
    }

    public static void isEmpty(@Nullable Map<?, ?> map, String message) {
        if (!CollectionUtils.isEmpty(map)) {
            throw new ServiceException(message);
        }
    }

    public static void isEmpty(@Nullable Map<?, ?> map, int code, String message) {
        if (!CollectionUtils.isEmpty(map)) {
            throw new ServiceException(code, message);
        }
    }

    public static void isEmpty(@Nullable Map<?, ?> map, ResultProvider provider) {
        if (!CollectionUtils.isEmpty(map)) {
            throw new ServiceException(provider);
        }
    }

    public static void isEmpty(@Nullable Map<?, ?> map, Supplier<String> messageSupplier) {
        if (!CollectionUtils.isEmpty(map)) {
            throw new ServiceException(nullSafeGet(messageSupplier));
        }
    }

    public static void isEmpty(@Nullable Map<?, ?> map, int code, Supplier<String> messageSupplier) {
        if (!CollectionUtils.isEmpty(map)) {
            throw new ServiceException(code, nullSafeGet(messageSupplier));
        }
    }

    public static void notEmpty(@Nullable Map<?, ?> map, String message) {
        if (CollectionUtils.isEmpty(map)) {
            throw new ServiceException(message);
        }
    }

    public static void notEmpty(@Nullable Map<?, ?> map, int code, String message) {
        if (CollectionUtils.isEmpty(map)) {
            throw new ServiceException(code, message);
        }
    }

    public static void notEmpty(@Nullable Map<?, ?> map, ResultProvider provider) {
        if (CollectionUtils.isEmpty(map)) {
            throw new ServiceException(provider);
        }
    }

    public static void notEmpty(@Nullable Map<?, ?> map, Supplier<String> messageSupplier) {
        if (CollectionUtils.isEmpty(map)) {
            throw new ServiceException(nullSafeGet(messageSupplier));
        }
    }

    public static void notEmpty(@Nullable Map<?, ?> map, int code, Supplier<String> messageSupplier) {
        if (CollectionUtils.isEmpty(map)) {
            throw new ServiceException(code, nullSafeGet(messageSupplier));
        }
    }

    public static void isInstanceOf(@NonNull Class<?> type, @Nullable Object obj, String message) {
        if (!type.isInstance(obj)) {
            throw new ServiceException(message);
        }
    }

    public static void isInstanceOf(@NonNull Class<?> type, @Nullable Object obj, int code, String message) {
        if (!type.isInstance(obj)) {
            throw new ServiceException(code, message);
        }
    }

    public static void isInstanceOf(@NonNull Class<?> type, @Nullable Object obj, ResultProvider provider) {
        if (!type.isInstance(obj)) {
            throw new ServiceException(provider);
        }
    }

    public static void isInstanceOf(@NonNull Class<?> type, @Nullable Object obj, Supplier<String> messageSupplier) {
        if (!type.isInstance(obj)) {
            throw new ServiceException(nullSafeGet(messageSupplier));
        }
    }

    public static void isInstanceOf(@NonNull Class<?> type, @Nullable Object obj, int code, Supplier<String> messageSupplier) {
        if (!type.isInstance(obj)) {
            throw new ServiceException(code, nullSafeGet(messageSupplier));
        }
    }

    public static void notInstanceOf(@NonNull Class<?> type, @Nullable Object obj, String message) {
        if (type.isInstance(obj)) {
            throw new ServiceException(message);
        }
    }

    public static void notInstanceOf(@NonNull Class<?> type, @Nullable Object obj, int code, String message) {
        if (type.isInstance(obj)) {
            throw new ServiceException(code, message);
        }
    }

    public static void notInstanceOf(@NonNull Class<?> type, @Nullable Object obj, ResultProvider provider) {
        if (type.isInstance(obj)) {
            throw new ServiceException(provider);
        }
    }

    public static void notInstanceOf(@NonNull Class<?> type, @Nullable Object obj, Supplier<String> messageSupplier) {
        if (type.isInstance(obj)) {
            throw new ServiceException(nullSafeGet(messageSupplier));
        }
    }

    public static void notInstanceOf(@NonNull Class<?> type, @Nullable Object obj, int code, Supplier<String> messageSupplier) {
        if (type.isInstance(obj)) {
            throw new ServiceException(code, nullSafeGet(messageSupplier));
        }
    }

    public static void isAssignable(@NonNull Class<?> superType, @Nullable Class<?> subType, String message) {
        if (subType == null || !superType.isAssignableFrom(subType)) {
            throw new ServiceException(message);
        }
    }

    public static void isAssignable(@NonNull Class<?> superType, @Nullable Class<?> subType, int code, String message) {
        if (subType == null || !superType.isAssignableFrom(subType)) {
            throw new ServiceException(code, message);
        }
    }

    public static void isAssignable(@NonNull Class<?> superType, @Nullable Class<?> subType, ResultProvider provider) {
        if (subType == null || !superType.isAssignableFrom(subType)) {
            throw new ServiceException(provider);
        }
    }

    public static void isAssignable(@NonNull Class<?> superType, @Nullable Class<?> subType, Supplier<String> messageSupplier) {
        if (subType == null || !superType.isAssignableFrom(subType)) {
            throw new ServiceException(nullSafeGet(messageSupplier));
        }
    }

    public static void isAssignable(@NonNull Class<?> superType, @Nullable Class<?> subType, int code, Supplier<String> messageSupplier) {
        if (subType == null || !superType.isAssignableFrom(subType)) {
            throw new ServiceException(code, nullSafeGet(messageSupplier));
        }
    }

    public static void notAssignable(@NonNull Class<?> superType, @Nullable Class<?> subType, String message) {
        if (subType != null && superType.isAssignableFrom(subType)) {
            throw new ServiceException(message);
        }
    }

    public static void notAssignable(@NonNull Class<?> superType, @Nullable Class<?> subType, int code, String message) {
        if (subType != null && superType.isAssignableFrom(subType)) {
            throw new ServiceException(code, message);
        }
    }

    public static void notAssignable(@NonNull Class<?> superType, @Nullable Class<?> subType, ResultProvider provider) {
        if (subType != null && superType.isAssignableFrom(subType)) {
            throw new ServiceException(provider);
        }
    }

    public static void notAssignable(@NonNull Class<?> superType, @Nullable Class<?> subType, Supplier<String> messageSupplier) {
        if (subType != null && superType.isAssignableFrom(subType)) {
            throw new ServiceException(nullSafeGet(messageSupplier));
        }
    }

    public static void notAssignable(@NonNull Class<?> superType, @Nullable Class<?> subType, int code, Supplier<String> messageSupplier) {
        if (subType != null && superType.isAssignableFrom(subType)) {
            throw new ServiceException(code, nullSafeGet(messageSupplier));
        }
    }

    @Nullable
    private static String nullSafeGet(@Nullable Supplier<String> messageSupplier) {
        return (messageSupplier != null ? messageSupplier.get() : null);
    }

}
