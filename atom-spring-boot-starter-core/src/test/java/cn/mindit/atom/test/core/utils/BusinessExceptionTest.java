/*
 * Copyright (c) 2022-2025 Catch(catchlife6@163.com).
 * Atom is licensed under Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.mindit.atom.test.core.utils;

import cn.mindit.atom.core.util.BusinessException;
import cn.mindit.atom.core.util.ResultProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * BusinessException 工具类测试
 *
 * @author Catch
 * @since 2023-08-08
 */
class BusinessExceptionTest {

    @Test
    @DisplayName("测试无参构造函数")
    void testNoArgConstructor() {
        BusinessException exception = new BusinessException();

        assertEquals(BusinessException.DEFAULT_CODE, exception.getCode());
        assertEquals(BusinessException.DEFAULT_MESSAGE, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("测试带消息的构造函数")
    void testMessageConstructor() {
        String message = "自定义错误消息";
        BusinessException exception = new BusinessException(message);

        assertEquals(BusinessException.DEFAULT_CODE, exception.getCode());
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("测试带代码和消息的构造函数")
    void testCodeAndMessageConstructor() {
        int code = 500;
        String message = "服务器内部错误";
        BusinessException exception = new BusinessException(code, message);

        assertEquals(code, exception.getCode());
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("测试带 ResultProvider 的构造函数")
    void testResultProviderConstructor() {
        TestResultProvider provider = new TestResultProvider(404, "资源未找到");
        BusinessException exception = new BusinessException(provider);

        assertEquals(provider.getCode(), exception.getCode());
        assertEquals(provider.getMessage(), exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("测试带 cause 的构造函数")
    void testCauseConstructor() {
        Throwable cause = new RuntimeException("原始异常");
        BusinessException exception = new BusinessException(cause);

        assertEquals(BusinessException.DEFAULT_CODE, exception.getCode());
        assertEquals(BusinessException.DEFAULT_MESSAGE, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("测试带消息和 cause 的构造函数")
    void testMessageAndCauseConstructor() {
        String message = "业务异常";
        Throwable cause = new RuntimeException("原始异常");
        BusinessException exception = new BusinessException(message, cause);

        assertEquals(BusinessException.DEFAULT_CODE, exception.getCode());
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("测试带代码、消息和 cause 的构造函数")
    void testCodeMessageAndCauseConstructor() {
        int code = 400;
        String message = "请求参数错误";
        Throwable cause = new RuntimeException("原始异常");
        BusinessException exception = new BusinessException(code, message, cause);

        assertEquals(code, exception.getCode());
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("测试带 ResultProvider 和 cause 的构造函数")
    void testResultProviderAndCauseConstructor() {
        TestResultProvider provider = new TestResultProvider(403, "权限不足");
        Throwable cause = new RuntimeException("原始异常");
        BusinessException exception = new BusinessException(provider, cause);

        assertEquals(provider.getCode(), exception.getCode());
        assertEquals(provider.getMessage(), exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("测试异常消息的传播")
    void testExceptionMessagePropagation() {
        String message = "测试消息";
        BusinessException exception = new BusinessException(message);

        assertEquals(message, exception.getMessage());
        assertEquals(message, exception.getLocalizedMessage());
    }

    @Test
    @DisplayName("测试异常原因的传播")
    void testExceptionCausePropagation() {
        Throwable cause = new RuntimeException("原始异常");
        BusinessException exception = new BusinessException(cause);

        assertEquals(cause, exception.getCause());
        assertEquals(cause.getMessage(), exception.getCause().getMessage());
    }

    @Test
    @DisplayName("测试默认常量")
    void testDefaultConstants() {
        assertEquals(400, BusinessException.DEFAULT_CODE);
        assertEquals("请求数据错误", BusinessException.DEFAULT_MESSAGE);
    }

    @Test
    @DisplayName("测试 ResultProvider 接口实现")
    void testResultProviderInterface() {
        int code = 401;
        String message = "未授权";
        BusinessException exception = new BusinessException(code, message);

        // 验证实现了 ResultProvider 接口
        assertTrue(exception instanceof ResultProvider);
        assertEquals(code, ((ResultProvider) exception).getCode());
        assertEquals(message, ((ResultProvider) exception).getMessage());
    }

    @Test
    @DisplayName("测试异常的字符串表示")
    void testExceptionToString() {
        BusinessException exception = new BusinessException(500, "服务器错误");
        String toString = exception.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("BusinessException"));
        assertTrue(toString.contains("服务器错误"));
    }

    @Test
    @DisplayName("测试异常的堆栈跟踪")
    void testExceptionStackTrace() {
        BusinessException exception = new BusinessException("测试异常");
        StackTraceElement[] stackTrace = exception.getStackTrace();

        assertNotNull(stackTrace);
        assertTrue(stackTrace.length > 0);
    }

    @Test
    @DisplayName("测试边界值 - 最小代码")
    void testEdgeCaseMinCode() {
        int minCode = Integer.MIN_VALUE;
        BusinessException exception = new BusinessException(minCode, "最小代码");

        assertEquals(minCode, exception.getCode());
    }

    @Test
    @DisplayName("测试边界值 - 最大代码")
    void testEdgeCaseMaxCode() {
        int maxCode = Integer.MAX_VALUE;
        BusinessException exception = new BusinessException(maxCode, "最大代码");

        assertEquals(maxCode, exception.getCode());
    }

    @Test
    @DisplayName("测试边界值 - 空消息")
    void testEdgeCaseEmptyMessage() {
        BusinessException exception = new BusinessException(400, "");

        assertEquals(400, exception.getCode());
        assertEquals("", exception.getMessage());
    }

    @Test
    @DisplayName("测试边界值 - null 消息")
    void testEdgeCaseNullMessage() {
        BusinessException exception = new BusinessException(400, null);

        assertEquals(400, exception.getCode());
        assertNull(exception.getMessage());
    }

    @Test
    @DisplayName("测试边界值 - null cause")
    void testEdgeCaseNullCause() {
        BusinessException exception = new BusinessException("消息", null);

        assertEquals("消息", exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("测试异常链")
    void testExceptionChaining() {
        Throwable rootCause = new RuntimeException("根原因");
        BusinessException middleException = new BusinessException("中间异常", rootCause);
        BusinessException topException = new BusinessException(500, "顶层异常", middleException);

        assertEquals("顶层异常", topException.getMessage());
        assertEquals(middleException, topException.getCause());
        assertEquals(rootCause, topException.getCause().getCause());
    }

    @Test
    @DisplayName("测试异常的 equals 和 hashCode")
    void testEqualsAndHashCode() {
        BusinessException exception1 = new BusinessException(400, "错误消息");
        BusinessException exception2 = new BusinessException(400, "错误消息");
        BusinessException exception3 = new BusinessException(500, "不同错误");

        // 相同的异常应该相等（基于引用）
        assertEquals(exception1, exception1);
        assertEquals(exception1.hashCode(), exception1.hashCode());

        // 不同的异常不应该相等
        assertNotEquals(exception1, exception2);
        assertNotEquals(exception1, exception3);

        // 与 null 比较
        assertNotEquals(null, exception1);

        // 与不同类型比较
        assertNotEquals(exception1, "字符串");
    }

    @Test
    @DisplayName("测试异常的 initCause")
    void testInitCause() {
        BusinessException exception = new BusinessException("初始异常");
        Throwable cause = new RuntimeException("后续原因");

        // 测试设置 cause
        exception.initCause(cause);
        assertEquals(cause, exception.getCause());
    }

    // 测试用的 ResultProvider 实现
    static class TestResultProvider implements ResultProvider {

        private final int code;
        private final String message;

        public TestResultProvider(int code, String message) {
            this.code = code;
            this.message = message;
        }

        @Override
        public int getCode() {
            return code;
        }

        @Override
        public String getMessage() {
            return message;
        }

    }

}