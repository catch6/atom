package cn.mindit.atom.test.core.util;

import cn.mindit.atom.core.util.Result;
import cn.mindit.atom.core.util.ServiceException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ServiceExceptionTest {

    @Test
    void defaultConstructorUsesDefaults() {
        ServiceException ex = new ServiceException();
        assertThat(ex.getCode()).isEqualTo(ServiceException.DEFAULT_CODE);
        assertThat(ex.getMessage()).isEqualTo(ServiceException.DEFAULT_MESSAGE);
    }

    @Test
    void messageConstructorKeepsDefaultCode() {
        ServiceException ex = new ServiceException("崩溃");
        assertThat(ex.getCode()).isEqualTo(ServiceException.DEFAULT_CODE);
        assertThat(ex.getMessage()).isEqualTo("崩溃");
    }

    @Test
    void codeMessageConstructorSetsBoth() {
        ServiceException ex = new ServiceException(5001, "服务异常");
        assertThat(ex.getCode()).isEqualTo(5001);
        assertThat(ex.getMessage()).isEqualTo("服务异常");
    }

    @Test
    void providerConstructorReadsFromProvider() {
        Result<Void> result = Result.fail(5002, "下游错误");
        ServiceException ex = new ServiceException(result);
        assertThat(ex.getCode()).isEqualTo(5002);
        assertThat(ex.getMessage()).isEqualTo("下游错误");
    }

    @Test
    void causeConstructorsWrapCause() {
        Throwable cause = new RuntimeException("root");
        ServiceException ex1 = new ServiceException(cause);
        ServiceException ex2 = new ServiceException("msg", cause);
        ServiceException ex3 = new ServiceException(5003, "msg", cause);
        ServiceException ex4 = new ServiceException(Result.fail(5004, "provider"), cause);

        assertThat(ex1.getCause()).isSameAs(cause);
        assertThat(ex1.getCode()).isEqualTo(ServiceException.DEFAULT_CODE);
        assertThat(ex2.getCause()).isSameAs(cause);
        assertThat(ex2.getMessage()).isEqualTo("msg");
        assertThat(ex3.getCode()).isEqualTo(5003);
        assertThat(ex4.getCode()).isEqualTo(5004);
        assertThat(ex4.getMessage()).isEqualTo("provider");
    }

    @Test
    void isRuntimeException() {
        assertThat(new ServiceException()).isInstanceOf(RuntimeException.class);
    }

}
