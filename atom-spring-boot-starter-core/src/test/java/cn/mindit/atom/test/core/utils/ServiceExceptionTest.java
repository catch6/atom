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

import cn.mindit.atom.core.util.ResultProvider;
import cn.mindit.atom.core.util.ServiceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ServiceException 工具类测试
 *
 * @author Catch
 * @since 2023-08-08
 */
class ServiceExceptionTest {

    @Test
    @DisplayName("测试无参构造函数")
    void testNoArgConstructor() {
        ServiceException exception = new ServiceException();

        assertEquals(ServiceException.DEFAULT_CODE, exception.getCode());
        assertEquals(ServiceException.DEFAULT_MESSAGE, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("测试带消息的构造函数")
    void testMessageConstructor() {
        String message = "服务内部错误";
        ServiceException exception = new ServiceException(message);

        assertEquals(ServiceException.DEFAULT_CODE, exception.getCode());
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("测试带代码和消息的构造函数")
    void testCodeAndMessageConstructor() {
        int code = 503;
        String message = "服务不可用";
        ServiceException exception = new ServiceException(code, message);

        assertEquals(code, exception.getCode());
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("测试带 ResultProvider 的构造函数")
    void testResultProviderConstructor() {
        TestResultProvider provider = new TestResultProvider(502, "网关错误");
        ServiceException exception = new ServiceException(provider);

        assertEquals(provider.getCode(), exception.getCode());
        assertEquals(provider.getMessage(), exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("测试带 cause 的构造函数")
    void testCauseConstructor() {
        Throwable cause = new RuntimeException("原始异常");
        ServiceException exception = new ServiceException(cause);

        assertEquals(ServiceException.DEFAULT_CODE, exception.getCode());
        assertEquals(ServiceException.DEFAULT_MESSAGE, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("测试带消息和 cause 的构造函数")
    void testMessageAndCauseConstructor() {
        String message = "服务异常";
        Throwable cause = new RuntimeException("原始异常");
        ServiceException exception = new ServiceException(message, cause);

        assertEquals(ServiceException.DEFAULT_CODE, exception.getCode());
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("测试带代码、消息和 cause 的构造函数")
    void testCodeMessageAndCauseConstructor() {
        int code = 501;
        String message = "服务未实现";
        Throwable cause = new RuntimeException("原始异常");
        ServiceException exception = new ServiceException(code, message, cause);

        assertEquals(code, exception.getCode());
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("测试带 ResultProvider 和 cause 的构造函数")
    void testResultProviderAndCauseConstructor() {
        TestResultProvider provider = new TestResultProvider(504, "网关超时");
        Throwable cause = new RuntimeException("原始异常");
        ServiceException exception = new ServiceException(provider, cause);

        assertEquals(provider.getCode(), exception.getCode());
        assertEquals(provider.getMessage(), exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("测试异常消息的传播")
    void testExceptionMessagePropagation() {
        String message = "测试消息";
        ServiceException exception = new ServiceException(message);

        assertEquals(message, exception.getMessage());
        assertEquals(message, exception.getLocalizedMessage());
    }

    @Test
    @DisplayName("测试异常原因的传播")
    void testExceptionCausePropagation() {
        Throwable cause = new RuntimeException("原始异常");
        ServiceException exception = new ServiceException(cause);

        assertEquals(cause, exception.getCause());
        assertEquals(cause.getMessage(), exception.getCause().getMessage());
    }

    @Test
    @DisplayName("测试默认常量")
    void testDefaultConstants() {
        assertEquals(500, ServiceException.DEFAULT_CODE);
        assertEquals("服务繁忙, 请稍后再试", ServiceException.DEFAULT_MESSAGE);
    }

    @Test
    @DisplayName("测试 ResultProvider 接口实现")
    void testResultProviderInterface() {
        int code = 501;
        String message = "服务未实现";
        ServiceException exception = new ServiceException(code, message);

        // 验证实现了 ResultProvider 接口
        assertTrue(exception instanceof ResultProvider);
        assertEquals(code, ((ResultProvider) exception).getCode());
        assertEquals(message, ((ResultProvider) exception).getMessage());
    }

    @Test
    @DisplayName("测试异常的字符串表示")
    void testExceptionToString() {
        ServiceException exception = new ServiceException(500, "服务器错误");
        String toString = exception.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("ServiceException"));
        assertTrue(toString.contains("服务器错误"));
    }

    @Test
    @DisplayName("测试异常的堆栈跟踪")
    void testExceptionStackTrace() {
        ServiceException exception = new ServiceException("测试异常");
        StackTraceElement[] stackTrace = exception.getStackTrace();

        assertNotNull(stackTrace);
        assertTrue(stackTrace.length > 0);
    }

    @Test
    @DisplayName("测试边界值 - 最小代码")
    void testEdgeCaseMinCode() {
        int minCode = Integer.MIN_VALUE;
        ServiceException exception = new ServiceException(minCode, "最小代码");

        assertEquals(minCode, exception.getCode());
    }

    @Test
    @DisplayName("测试边界值 - 最大代码")
    void testEdgeCaseMaxCode() {
        int maxCode = Integer.MAX_VALUE;
        ServiceException exception = new ServiceException(maxCode, "最大代码");

        assertEquals(maxCode, exception.getCode());
    }

    @Test
    @DisplayName("测试边界值 - 空消息")
    void testEdgeCaseEmptyMessage() {
        ServiceException exception = new ServiceException(500, "");

        assertEquals(500, exception.getCode());
        assertEquals("", exception.getMessage());
    }

    @Test
    @DisplayName("测试边界值 - null 消息")
    void testEdgeCaseNullMessage() {
        ServiceException exception = new ServiceException(500, null);

        assertEquals(500, exception.getCode());
        assertNull(exception.getMessage());
    }

    @Test
    @DisplayName("测试边界值 - null cause")
    void testEdgeCaseNullCause() {
        ServiceException exception = new ServiceException("消息", null);

        assertEquals("消息", exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("测试异常链")
    void testExceptionChaining() {
        Throwable rootCause = new RuntimeException("根原因");
        ServiceException middleException = new ServiceException("中间异常", rootCause);
        ServiceException topException = new ServiceException(500, "顶层异常", middleException);

        assertEquals("顶层异常", topException.getMessage());
        assertEquals(middleException, topException.getCause());
        assertEquals(rootCause, topException.getCause().getCause());
    }

    @Test
    @DisplayName("测试异常的 equals 和 hashCode")
    void testEqualsAndHashCode() {
        ServiceException exception1 = new ServiceException(500, "错误消息");
        ServiceException exception2 = new ServiceException(500, "错误消息");
        ServiceException exception3 = new ServiceException(400, "不同错误");

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
        ServiceException exception = new ServiceException("初始异常");
        Throwable cause = new RuntimeException("后续原因");

        // 测试设置 cause
        exception.initCause(cause);
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("测试 ServiceException 与 BusinessException 的区别")
    void testDifferenceFromBusinessException() {
        // ServiceException 用于服务层异常，默认代码500
        cn.mindit.atom.core.util.BusinessException businessException =
            new cn.mindit.atom.core.util.BusinessException(400, "业务异常");
        ServiceException serviceException = new ServiceException(500, "服务异常");

        assertEquals(400, businessException.getCode());
        assertEquals(500, serviceException.getCode());
        assertEquals("业务异常", businessException.getMessage());
        assertEquals("服务异常", serviceException.getMessage());
    }

    @Test
    @DisplayName("测试多级异常链")
    void testMultiLevelExceptionChain() {
        Throwable rootCause = new RuntimeException("数据库连接失败");
        ServiceException dbException = new ServiceException(503, "数据库服务不可用", rootCause);
        ServiceException serviceException = new ServiceException(500, "服务调用失败", dbException);

        assertEquals("服务调用失败", serviceException.getMessage());
        assertEquals(dbException, serviceException.getCause());
        assertEquals(rootCause, serviceException.getCause().getCause());

        // 验证根原因消息
        assertEquals("数据库连接失败", serviceException.getCause().getCause().getMessage());
    }

    @Test
    @DisplayName("测试 ResultProvider 的异常构造")
    void testResultProviderExceptionConstruction() {
        TestResultProvider provider = new TestResultProvider(502, "网关错误");
        ServiceException exception1 = new ServiceException(provider);
        ServiceException exception2 = new ServiceException(provider, new RuntimeException("网络超时"));

        // 测试基本构造
        assertEquals(502, exception1.getCode());
        assertEquals("网关错误", exception1.getMessage());
        assertNull(exception1.getCause());

        // 测试带cause的构造
        assertEquals(502, exception2.getCode());
        assertEquals("网关错误", exception2.getMessage());
        assertNotNull(exception2.getCause());
        assertEquals("网络超时", exception2.getCause().getMessage());
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